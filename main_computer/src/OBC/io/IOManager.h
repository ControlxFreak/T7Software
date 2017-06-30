/*------------------------------------------------------------------------------
Function Name: IOManager.h

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
    15 Feb 2017 - t3 - Happy Birthday!
--------------------------------------------------------------------------------
 */

#ifndef IOMANAGER_H
#define IOMANAGER_H


#include "DataManager.h"
#include "LogManager.h"
#include "tcpstream.h"
#include "T7Types.h"
#include <string>
#include <thread>
#include <map>

#include <stdlib.h>
#include <Python.h>
#include <sys/socket.h>
#include <linux/wireless.h>
#include <sys/ioctl.h>
#include <stdio.h>
#include <cstring>
#include <cstdlib>
#include <unistd.h>

#include "tcpacceptor.h"
#include "tcpconnector.h"
#include "T7Messages.pb.h"
#include <google/protobuf/io/zero_copy_stream_impl.h>

using namespace std;

class IOManager {
public:
    
    // Store singleton parameters
    DataManager* data; 
    LogManager* LM;
    
    // Define the IO parameters needed for communication
    string HSS_IP = "127.0.0.1";
    int CLIENT_PORT_NUMBER = 9002;
    int SERVER_PORT_NUMBER = 9001;
    
    int CLIENT_TIMEOUT = 1e6;
    
    void launch();
    void launch_clients();
    void launch_server();
    void launch_serial();
    void client_handler(int id);
    void relaunch_client(int id);
    void server_handler();
    void wifi_handler();
    void thermal_array_handler();
    void pixhawk_handler();
    void acceptor_handler(TCPStream*,int id, T7::GenericMessage GM);
    void pre_acceptor_handler(TCPStream*);
    void clean();
    
    bool writeDelimitedTo(T7::GenericMessage message, google::protobuf::io::ZeroCopyOutputStream* rawOutput);
    
    bool readDelimitedFrom(google::protobuf::io::ZeroCopyInputStream* rawInput, T7::GenericMessage* message);
    // Static singleton initializer 
    static IOManager* getInstance() {
        static IOManager* p_IOManager = new IOManager();
        return p_IOManager;
    };
    
    IOManager();
    IOManager(const IOManager& orig);
    virtual ~IOManager();
    
private:
    map<int,thread*>sockThreadMap;
    vector<TCPStream*>streams;    
    
};

#endif /* IOManager */

