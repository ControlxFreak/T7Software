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
#include "tcpconnector.h"
#include "tcpacceptor.h"
#include "T7Messages.pb.h"
#include <boost/thread/thread.hpp>
#include <string>

class IOManager {
public:
    // Define the IO parameters needed for communication
    int PORT_NUMBER = 9001;
    string HSS_IP = "127.0.0.1";
    int ACCEPTOR_TIMEOUT = 5;
    int CONNECTOR_TIMEOUT = 5;
    int SLEEP_TIME = 3000;
    
    DataManager* data; 
    LogManager* LM;
    
    bool timeToDie = false; 
    
    void kill(){};
    void clean(){timeToDie = true;};
    void launch_sensor(){};
    void socketHandler(int id);
    IOManager();
    IOManager(const IOManager& orig);
    virtual ~IOManager();
private:
      
};

#endif /* IOManager */

