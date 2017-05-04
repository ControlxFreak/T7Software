/*
   TCPConnector.h

   TCPConnector class definition. TCPConnector provides methods to actively
   establish TCP/IP connections with a server.

   ------------------------------------------

   Copyright (c) 2013 Vic Hargrave

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License
*/

#include <stdio.h>
#include <string.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <errno.h>
#include "tcpconnector.h"

TCPStream* TCPConnector::connect(const char* server, int port)
{
    LM = LogManager::getInstance();
    struct sockaddr_in address;
    char c_err[100];
    string err;
    
    memset (&address, 0, sizeof(address));
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    if (resolveHostName(server, &(address.sin_addr)) != 0 ) {
        inet_pton(PF_INET, server, &(address.sin_addr));        
    } 

    // Create and connect the socket, bail if we fail in either case
    int sd = socket(AF_INET, SOCK_STREAM, 0);
    if (sd < 0) {
        sprintf(c_err,"ERROR: Socket %d failed",m_id);
        err = c_err;
        LM->append(err);
        return NULL;
    }
    if (::connect(sd, (struct sockaddr*)&address, sizeof(address)) != 0) {
        sprintf(c_err,"ERROR: Connect %d failed",m_id);
        err = c_err;
        LM->append(err);
        close(sd);
        return NULL;
    }
    return new TCPStream(sd, &address,m_id);
}

TCPStream* TCPConnector::connect(const char* server, int port, int timeout)
{
    LM = LogManager::getInstance();
    if (timeout == 0) return connect(server, port);
 
    char c_err[100];
    string err;
    
    struct sockaddr_in address;

    memset (&address, 0, sizeof(address));
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    if (resolveHostName(server, &(address.sin_addr)) != 0 ) {
        inet_pton(PF_INET, server, &(address.sin_addr));        
    }     

    long arg;
    fd_set sdset;
    struct timeval tv;
    socklen_t len;
    int result = -1, valopt, sd = socket(AF_INET, SOCK_STREAM, 0);
    
    // Bail if we fail to create the socket
    if (sd < 0) {
        sprintf(c_err,"ERROR: Socket %d failed",m_id);
        err = c_err;
        LM->append(err);
        return NULL;
    }    

    // Set socket to non-blocking
    arg = fcntl(sd, F_GETFL, NULL);
    arg |= O_NONBLOCK;
    fcntl(sd, F_SETFL, arg);
    
    // Connect with time limit
    string message;
    if ((result = ::connect(sd, (struct sockaddr *)&address, sizeof(address))) < 0) 
    {
        if (errno == EINPROGRESS)
        {
            tv.tv_sec = timeout;
            tv.tv_usec = 0;
            FD_ZERO(&sdset);
            FD_SET(sd, &sdset);
            int s = -1;
            do {
                s = select(sd + 1, NULL, &sdset, NULL, &tv);
            } while (s == -1 && errno == EINTR);
            if (s > 0)
            {
                len = sizeof(int);
                getsockopt(sd, SOL_SOCKET, SO_ERROR, (void*)(&valopt), &len);
                if (valopt) {
                    sprintf(c_err,"connect()<id:%d> error %d - %s\n", m_id,valopt, strerror(valopt));
                    err = c_err;
                    LM->append(err);
                }
                // connection established
                else result = 0;
            }
            else {
                sprintf(c_err,"connect()<id:%d> timeout\n", m_id);
                err = c_err;
                LM->append(err);
            }
        }
        else 
            {
                sprintf(c_err,"connect()<id:%d>error %d - %s\n", m_id,errno, strerror(errno));
                err = c_err;
                LM->append(err);
            }
    }

    // Return socket to blocking mode 
    arg = fcntl(sd, F_GETFL, NULL);
    arg &= (~O_NONBLOCK);
    fcntl(sd, F_SETFL, arg);

    // Create stream object if connected
    if (result == -1) return NULL;
    return new TCPStream(sd, &address,m_id);
}

int TCPConnector::resolveHostName(const char* hostname, struct in_addr* addr) 
{
    struct addrinfo *res;
  
    int result = getaddrinfo (hostname, NULL, NULL, &res);
    if (result == 0) {
        memcpy(addr, &((struct sockaddr_in *) res->ai_addr)->sin_addr, sizeof(struct in_addr));
        freeaddrinfo(res);
    }
    return result;
}
