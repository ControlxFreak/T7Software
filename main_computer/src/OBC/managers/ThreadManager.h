/*------------------------------------------------------------------------------
Function Name: CoreProcessor.h

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
/* 
 * File:   ThreadManager.h
 * Author: controlxfreak
 *
 * Created on April 7, 2017, 11:30 AM
 */

#ifndef THREADMANAGER_H
#define THREADMANAGER_H
#include <map>
#include <boost/thread.hpp>
#include "WatchDog.h"
#include "IOManager.h"
#include "LogManager.h"
#include "DataManager.h"

using namespace std;
class ThreadManager {
public:
    DataManager* data; 
    void launch(IOManager*);
    void clean();
    ThreadManager();
    ThreadManager(const ThreadManager& orig);
    virtual ~ThreadManager();
private:
    void socketHandler(IOManager*,int);    
    void sensorHandler(IOManager*);
    
    map<int, boost::thread*> threadMap;
    enum threadKeys { 
                   // Sockets Threads //
                   // Server //
                   HeartSock,
                   TerminateSock,
                   UParamsSock,
                   ConfigSock,
                   MoveCamSock,

                   // Client //
                   AccelSock,
                   GyroSock,
                   AltSock,
                   AttSock,
                   TempSock,
                   BatSock,

                   // Sensor Threads //
                   SensorKey 
                 };
};

#endif /* THREADMANAGER_H */

