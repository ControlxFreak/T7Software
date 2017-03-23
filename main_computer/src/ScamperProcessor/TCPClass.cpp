/*------------------------------------------------------------------------------
Function Name: TCPClass.cpp
 * This is a generic TCP class that handles socket responsibilities and tcp 
 * messaging.
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
void TCPClass::run_server(){
          
    // Define Local Params
    int header_bytes_read = 0;          // Actual number of header bytes read 
    int data_bytes_read = 0;            // Actual number of data bytes read
    char buf[MAX_BUFFER_SIZE];          // Allocate the buffer
    int MID;                            // Initialize the Message ID
    std::string sMID;                   // Initialize the string message id
    int n_data_bytes;                   // Number of data bytes we should see
    std::vector<std::string> data;      // Initialize the data vector
    
    // Loop until we have receive the kill message
    while (!KYS) {

        // Try to accept a connection with the socket
        // TODO: 20170227 t3 - Need to add retries
        if ((receiveSocket.socketConnection = accept(receiveSocket.socketHandle, NULL, NULL)) < 0) {
            std::cout<<"Failed to accept receive socket\n";
            break;
        }
        // First read the header
        header_bytes_read = recv(receiveSocket.socketConnection,buf,HEADER_SIZE,0);
        
        // Check to see if we read as many header bytes as we expected.
        // TODO: 20170227 t3 - if not, clear the buffer and ask for a re-send
        if (header_bytes_read != HEADER_SIZE){
            std::cout<<"Failed to read that header... Possible Retry!?\n";
            break;
        }
        
        buf[header_bytes_read] = (char) NULL; // Null terminate string
        
        // Parse out the MID and Data length
        // Cast the elements in the array to substrings 
        std::string bufstr(buf);
        sMID = bufstr.substr(0,3);
        MID = stoi(sMID);
        n_data_bytes = stoi(bufstr.substr(3,4));
                       
        // KYS if MID == 666
        if(MID == 666) break;
                
        // Read the actual data from the buffer.        
        data_bytes_read = recv(receiveSocket.socketConnection, buf, n_data_bytes, 0);
        // TODO: 20170227 t3 - if not, clear the buffer and ask for a re-send
        if (data_bytes_read != n_data_bytes){
            std::cout<<"Failed to read that data buffer... Possible Retry!?\n";
            break;
        }

        buf[data_bytes_read] = (char) NULL; // Null terminate string
        
        // Print what we've got!
        char cons_msg[50+data_bytes_read];
        sprintf(cons_msg,"Number of Bytes Received: %d\n Received: %s\n",data_bytes_read,buf);
        std::cout<<cons_msg;
        
        // Save the buffer as a string.
        bufstr = buf;
        
        // Save it to the data queue and keep moving!
        data.clear();
        
        data.push_back(sMID);
        data.push_back(buf);
        
        set_data(data);
    } // !kys
    
    std::cout<<"TCP receive() closed the socket.\n";
    kill();
    close(receiveSocket.socketHandle);    
} // send
//----------------------------------------------------------------------------//
void TCPClass::send_msg(){

    

} // receive()

//----------------------------------------------------------------------------//
void TCPClass::init_connection(){
    
    std::cout<<"Setting Up TCP Sockets!\n";
    
    setupSocket(&receiveSocket);
    setupSocket(&sendSocket);
 
} // init_connection()

void TCPClass::setupSocket(sockStruct* sock){
    
    // Clear structure memory
    bzero(&sock->socketInfo, sizeof(sockaddr_in));  
    
    // Get system information
    gethostname(sock->sysHost, MAXHOSTNAME);  // Get the name of this computer we are running on
    if((sock->hPtr = gethostbyname(sock->sysHost)) == NULL)
    {
       std::cerr << "System hostname misconfigured." << std::endl;
       exit(EXIT_FAILURE);
    }
    
    // create socket
    if((sock->socketHandle = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
       std::cout<<"Error Opening the Socket.";
       close(sock->socketHandle);
       exit(EXIT_FAILURE);
    }

    // make the socket reusable
    int option = 1;
    setsockopt(sock->socketHandle,SOL_SOCKET,SO_REUSEADDR,&option, sizeof(option));


    // Load system information into socket data structures

    sock->socketInfo.sin_family = AF_INET;
    sock->socketInfo.sin_addr.s_addr = htonl(INADDR_ANY);             // Use any address available to the system
    sock->socketInfo.sin_port = htons(sock->portNumber);      // Set port number

    // Bind the socket to a local socket address
     std::cout<<"Binding to the TCP socket!\n";
    if( bind(sock->socketHandle, (struct sockaddr *) &sock->socketInfo, sizeof(sock->socketInfo)) < 0)
    {

       std::cout<<"Error Binding to the Socket.";
       close(sock->socketHandle);
       perror("bind");
       exit(EXIT_FAILURE);
    }
     
    // Set the listen flag on the socket to true.
    listen(sock->socketHandle, 1);
}

//----------------------------------------------------------------------------//
void TCPClass::kill(){
    std::cout<<"TCP Kill Message Received.\nByeBye.\n";
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
    receiveSocket.portNumber = MP->ComParams.tcp_rec_port;
    sendSocket.portNumber = MP->ComParams.tcp_sen_port;
    
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

