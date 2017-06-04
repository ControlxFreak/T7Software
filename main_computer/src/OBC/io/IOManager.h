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

using namespace std;

class IOManager {
public:
    
    // Store singleton parameters
    DataManager* data; 
    LogManager* LM;
    
    // Define the IO parameters needed for communication
    string HSS_IP = "127.0.0.1";
    int CLIENT_PORT_NUMBER = 9001;
    int SERVER_PORT_NUMBER = 9002;
    
    int CLIENT_TIMEOUT = 1e6;
    
    void launch();
    void launch_clients();
    void launch_server();
    void launch_serial();
    void client_handler(int id);
    void relaunch_client(int id);
    void server_handler();
    void acceptor_handler(TCPStream*,int id);
    void clean();
    
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
};

#endif /* IOManager */

