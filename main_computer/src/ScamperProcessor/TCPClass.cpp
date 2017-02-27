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
          
    int socketConnection;

    while (!KYS) {

        if ((socketConnection = accept(socketHandle, NULL, NULL)) < 0) {
            write_to_console("Failed to accept receive socket");
            exit(EXIT_FAILURE);
        }

        int rc = 0; // Actual number of bytes read
        char buf[512];

        // rc is the number of characters returned.
        // Note this is not typical. Typically one would only specify the number 
        // of bytes to read a fixed header which would include the number of bytes
        // to read. See "Tips and Best Practices" below.

        rc = recv(socketConnection, buf, 512, 0);
        buf[rc] = (char) NULL; // Null terminate string
        
        char cons_msg[100];
        sprintf(cons_msg,"Number of Bytes Received: %d\n Received: %s\n",rc,buf);
        write_to_console(cons_msg);
        
        set_data(buf);
        
    }
    write_to_console("TCP receive() closed the socket.");
    close(socketHandle);    
} // send
//----------------------------------------------------------------------------//
void TCPClass::send_loop(){


} // receive()

//----------------------------------------------------------------------------//
void TCPClass::init_connection(){
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
   if( bind(socketHandle, (struct sockaddr *) &socketInfo, sizeof(socketInfo)) < 0)
   {
       
      std::cout<<"Error Binding to the Socket.";
      close(socketHandle);
      perror("bind");
      exit(EXIT_FAILURE);
   }

    bzero(&remoteSocketInfo, sizeof(sockaddr_in));  // Clear structure memory

    // Load system information into socket data structures

    memcpy((char *)&remoteSocketInfo.sin_addr, hPtr->h_addr, hPtr->h_length);
    remoteSocketInfo.sin_family = AF_INET;
    remoteSocketInfo.sin_port = htons((u_short)tcp_port);      // Set port number
   
  
   // Set the listen flag on the socket to true.
   listen(socketHandle, 1);
   
} // init_connection()
//----------------------------------------------------------------------------//
void TCPClass::stop(){
    KYS = true;
}// stop()

//----------------------------------------------------------------------------//
void TCPClass::set_data(char* new_data){
    datalock.lock(); 
    dataqueue.push(new_data);
    datalock.unlock();
} // set_data
char* TCPClass::get_data(){
    datalock.lock();
    char* outdata = dataqueue.front();
    dataqueue.back();
    datalock.unlock();
    return outdata;
} // get_data

//----------------------------------------------------------------------------//
void TCPClass::set_params( const char* MP_tcp_port){
    
    std::stringstream strValue;
    strValue << MP_tcp_port;
    strValue >> tcp_port;
   
    bzero(&socketInfo, sizeof(sockaddr_in));  // Clear structure memory
    
    // Get system information
    gethostname(sysHost, MAXHOSTNAME);  // Get the name of this computer we are running on
    if((hPtr = gethostbyname(sysHost)) == NULL)
    {
       std::cerr << "System hostname misconfigured." << std::endl;
       exit(EXIT_FAILURE);
    }
}

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

