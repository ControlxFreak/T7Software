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



#define MAXHOSTNAME 256
class TCPClass {
public:
    //------------------------------------------------------------------------//
    // Properties
    uint16_t tcp_port;
    bool connected = false;
    
    struct sockaddr_in socketInfo;
    struct sockaddr_in remoteSocketInfo;
    
    char sysHost[MAXHOSTNAME+1];  
    struct hostent *hPtr;
    int socketHandle;
    bool KYS = false;

    std::mutex datalock;
    
    std::queue<char*> dataqueue;    
    
    std::mutex consolelock;

    
    //------------------------------------------------------------------------//
    //Methods
    void set_params(const char*);
    
    void send_loop();
    void init_send();
    void receive_loop();
    void init_recieve();
    void init_connection();
    void stop();
    void reconnect();
    void set_data(char*);
    char* get_data();    
    
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

