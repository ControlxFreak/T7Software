/*------------------------------------------------------------------------------
Function Name: TCPClass.cpp

--------------------------------------------------------------------------------
Inputs:
 * N/a
Outputs:
 * N/a
--------------------------------------------------------------------------------
Properties:
 * N/a
Methods:
 * N/a
--------------------------------------------------------------------------------
Lockheed Martin 
Engineering Leadership Development Program
Team 7
15 February 2017
Anthony Trezza
--------------------------------------------------------------------------------
Change Log
    25 Feb 2017 - t3 - Happy Birthday!
--------------------------------------------------------------------------------
 */
#include "TCPClass.h"


//----------------------------------------------------------------------------//
void TCPClass::receive_loop(){
          
    // Define Local Params
    int header_bytes_read = 0;           // Actual number of header bytes read 
    int data_bytes_read = 0;         // Actual number of data bytes read
    char buf[max_buffer_size];  // buffer
    char AKG[8];
    std::string MID;           // Message ID
    int n_data_bytes;   // Number of data bytes we should see
    std::vector<std::string> data;
    
    // Loop until we have receive the kill message
    while (!KYS) {

        // Try to accept a connection with the socket
        // TODO: 20170227 t3 - Need to add retries
        if ((socketConnection = accept(socketHandle, NULL, NULL)) < 0) {
            std::cout<<"Failed to accept receive socket\n";
            break;
        }
        // First read the header... Be sure to wait for the entire header to be there first!
        header_bytes_read = recv(socketConnection,buf,header_size,0);
        
        // Check to see if we read as many header bytes as we expected.
        // TODO: 20170227 t3 - if not, clear the buffer and ask for a re-send
        if (header_bytes_read != header_size){
            std::cout<<"Failed to read that header... Possible Retry!?\n";
            break;
        }
        
        buf[header_bytes_read] = (char) NULL; // Null terminate string
        
        // Parse out the MID and Data length
        // Cast the elements in the array to substrings 
        std::string bufstr(buf);
        
        MID = bufstr.substr(0,3);
        n_data_bytes = stoi(bufstr.substr(3,4));  // cast it to an int
               
        // NOTE: The header_size does not have to equal 5.  In this case, it does though.
        //       Future work could include implementing a template that maps the bytes to a 
        //       framework.  Not in scope for scamper.
        
        
        // KYS if MID == 666
        if(stoi(MID) == 666) kill();
                
        // Read the actual data from the buffer.        
        data_bytes_read = recv(socketConnection, buf, n_data_bytes, 0);
        // TODO: 20170227 t3 - if not, clear the buffer and ask for a re-send
        if (data_bytes_read != n_data_bytes){
            std::cout<<"Failed to read that data buffer... Possible Retry!?\n";
            break;
        }

        buf[data_bytes_read] = (char) NULL; // Null terminate string
        
        // Print what we got!
        char cons_msg[50+data_bytes_read];
        sprintf(cons_msg,"Number of Bytes Received: %d\n Received: %s\n",data_bytes_read,buf);
        std::cout<<cons_msg;
        
        // Save the buffer as a string.
        bufstr = buf;
        
        // TODO: 20170227 t3 - send an AKG message!
        // Create the AKG message
        /*
        sprintf(AKG,"00003");
        AKG[5] = MID[0];
        AKG[6] = MID[1];
        AKG[7] = MID[2];
        
        send(socketHandle,AKG,9,0);
        */
        
        // Save it to the data queue and keep moving!
        data.clear();
        
        data.push_back(MID);
        data.push_back(buf);
        
        set_data(data);
    }
    std::cout<<"TCP receive() closed the socket.\n";
    kill();
    close(socketHandle);    
} // send
//----------------------------------------------------------------------------//
void TCPClass::send_msg(){


} // receive()

//----------------------------------------------------------------------------//
void TCPClass::init_connection(){
    
    std::cout<<"Creating the TCP socket!\n";
    // create socket
   if((socketHandle = socket(AF_INET, SOCK_STREAM, 0)) < 0)
   {
      std::cout<<"Error Opening the Socket.";
      close(socketHandle);
      exit(EXIT_FAILURE);
   }
   
   // make the socket reusable
   int option = 1;
   setsockopt(socketHandle,SOL_SOCKET,SO_REUSEADDR,&option, sizeof(option));
   
   
   // Load system information into socket data structures

   socketInfo.sin_family = AF_INET;
   socketInfo.sin_addr.s_addr = htonl(INADDR_ANY); // Use any address available to the system
   socketInfo.sin_port = htons(tcp_port);      // Set port number

   // Bind the socket to a local socket address
    std::cout<<"Binding to the TCP socket!\n";
   if( bind(socketHandle, (struct sockaddr *) &socketInfo, sizeof(socketInfo)) < 0)
   {
       
      std::cout<<"Error Binding to the Socket.";
      close(socketHandle);
      perror("bind");
      exit(EXIT_FAILURE);
   }

   /*
    bzero(&remoteSocketInfo, sizeof(sockaddr_in));  // Clear structure memory

    // Load system information into socket data structures
    memcpy((char *)&remoteSocketInfo.sin_addr, hPtr->h_addr, hPtr->h_length);
    remoteSocketInfo.sin_family = AF_INET;
    remoteSocketInfo.sin_port = htons((u_short)tcp_port);      // Set port number
   */
  
   // Set the listen flag on the socket to true.
   listen(socketHandle, 1);
} // init_connection()
//----------------------------------------------------------------------------//
void TCPClass::kill(){
    std::cout<<"Kill Message Received.\nByeBye.\n";
    KYS = true;
}// kill()

//----------------------------------------------------------------------------//
void TCPClass::set_data(std::vector<std::string> new_data){
    datalock.lock(); 
    dataqueue.push(new_data);
    datalock.unlock();
} // set_data

//----------------------------------------------------------------------------//
std::vector<std::string> TCPClass::get_data(){
    datalock.lock();

    std::vector<std::string> outdata = dataqueue.front();
    dataqueue.pop();
    datalock.unlock();
    return outdata;
} // get_data

//----------------------------------------------------------------------------//
void TCPClass::set_params( MissionParameters* MP){
    
    // Grab the TCP Port number and cast it to an int
    std::stringstream strValue;
    strValue << MP->tcp_port;
    strValue >> tcp_port;
    
    // Grab the Max Buffer Size and cast it to an int
    strValue.clear();
    strValue << MP->max_buffer_size;
    strValue >> max_buffer_size;
    
    // Grab the Max Buffer Size and cast it to an int
    strValue.clear();
    strValue << MP->header_size;
    strValue >> header_size;
   
    // Clear structure memory
    bzero(&socketInfo, sizeof(sockaddr_in));  
    
    // Get system information
    gethostname(sysHost, MAXHOSTNAME);  // Get the name of this computer we are running on
    if((hPtr = gethostbyname(sysHost)) == NULL)
    {
       std::cerr << "System hostname misconfigured." << std::endl;
       exit(EXIT_FAILURE);
    }
} // set_params

void TCPClass::write_to_console(const char* output){
    consolelock.lock();
    std::cout<<output;
    consolelock.unlock();    
}

void TCPClass::write_to_console( char* output)
{
    consolelock.lock();
    std::cout<<output;
    consolelock.unlock();    
    
} // multithread_console_message

int TCPClass::num_data(){
    datalock.lock();
    int num = dataqueue.size();
    datalock.unlock();
    return num;
}

//----------------------------------------------------------------------------//
// Constructors and Destructors
TCPClass::TCPClass() {     
}


TCPClass::TCPClass(const TCPClass& orig) {
}

TCPClass::~TCPClass() {
}

