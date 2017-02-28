/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   TCPClass.h
 * Author: controlxfreak
 *
 * Created on February 24, 2017, 3:13 PM
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
class TCPClass {
public:
    //------------------------------------------------------------------------//
    // Properties
    
    // Connection and Socket Properties
    uint16_t tcp_port;              // port number
    struct sockaddr_in socketInfo;  // socket information
    char sysHost[MAXHOSTNAME+1];    // system host name
    struct hostent *hPtr;           // pointer to the host IP
    int socketHandle;               // Socket handle
    uint16_t max_buffer_size;       // Max buffer size
    bool connected = false;         // Connected flag (true = connected, false = disconnected)
    uint16_t header_size;
    int socketConnection; 
    
    // Commands
    bool KYS = false;               // KYS flag is what is used to decided when to kill the threads

    // Data
    std::mutex datalock;            // datalock prevents race conditions on the data queue from multiple threads
    std::mutex consolelock;         // consolelock prevents race conditions when writting to the console from multiple threads
    
    std::queue<std::vector<std::string>> dataqueue;    // dataqueue stores all of the data

    
    //------------------------------------------------------------------------//
    //Methods
    void set_params(MissionParameters*);
    
    void send_msg();
    void receive_loop();
    void init_connection();
    void kill();
    void reconnect();
    void set_data(std::vector<std::string>);
    std::vector<std::string> get_data();    
    
    int num_data();
    void write_to_console(char*);
    void write_to_console(const char*);
    
    //------------------------------------------------------------------------//
    // Constructors and Destructors
    TCPClass();
    TCPClass(const TCPClass& orig);
    virtual ~TCPClass();
private:
};

#endif /* TCPCLASS_H */

