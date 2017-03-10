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

#ifndef TCPCLASS_H
#define TCPCLASS_H

#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <strings.h>
#include <stdio.h>
#include <unistd.h>
#include <sstream>
#include <queue>  
#include <mutex>
#include <string.h>
#include <vector>

#include "MissionParameters.h"


#define MAXHOSTNAME 256
#define MAX_BUFFER_SIZE 512
#define HEADER_SIZE 5

struct sockStruct{ 
    uint16_t portNumber;                    // receive port number
    struct sockaddr_in socketInfo;          // receive socket information
    struct hostent *hPtr;                   // pointer to the host IP
    int socketHandle;                       // Socket handle
    char sysHost[MAXHOSTNAME+1];            // system host name
    int socketConnection;                   //  Socket Connection
    }; // sockStruct

class TCPClass {
public:
    //------------------------------------------------------------------------//
    // Properties
    
    // Connection and Socket Properties
    sockStruct receiveSocket;
    sockStruct sendSocket;
    
    // General Commands
    bool KYS = false;               // KYS flag is what is used to decided when to kill the threads

    // Mutli-threading Locks
    std::mutex datalock;            // datalock prevents race conditions on the data queue from multiple threads
    std::mutex consolelock;         // consolelock prevents race conditions when writing to the console from multiple threads
    
    // Data Queue
    std::queue<std::vector<std::string>> dataqueue;    // dataqueue stores all of the data

    //------------------------------------------------------------------------//
    //Methods
    void set_params(MissionParameters*);
    
    void send_msg();
    void run_server();
    void init_connection();
    void kill();
    void reconnect();
    void set_data(std::vector<std::string>);
    std::vector<std::string> get_data();    
    
    int num_data();
    void write_to_console(char*);
    void write_to_console(const char*);
    void setupSocket(sockStruct*);
    
    //------------------------------------------------------------------------//
    // Constructors and Destructors
    TCPClass();
    TCPClass(const TCPClass& orig);
    virtual ~TCPClass();
private:
};

#endif /* TCPCLASS_H */

