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


#ifndef THREADMANAGER_H
#define THREADMANAGER_H
#include <map>
#include <thread>
#include "WatchDog.h"
#include "IOManager.h"
#include "LogManager.h"
#include "DataManager.h"

using namespace std;
class ThreadManager {
public:
    
    DataManager* data; 
    IOManager* IO;
    LogManager* LM;
    WatchDog* WD;
    
    void launch();
    void launch_tcp();
    void launch_serial();
    
    void clean();
    ThreadManager();
    ThreadManager(const ThreadManager& orig);
    virtual ~ThreadManager();
private:
    void serverSocketHandler();    
    void clientSocketHandler();
    void sensorHandler();
    
    map<int, thread*> threadMap;
    
    enum 
    threadKeys { 
                   // Sockets Threads //
                   // Server //
                   HeartSock,
                   TerminateSock,
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

