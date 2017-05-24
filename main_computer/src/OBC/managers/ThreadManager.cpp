/*------------------------------------------------------------------------------
Function Name: ThreadManager.cpp

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
    21 May 2017 - t3 - Updated for ServerSocket / ClientSocket stuff
--------------------------------------------------------------------------------
 */

#include "ThreadManager.h"


//----------------------------------------------------------------------------//
// launch() launches all of the threads
//----------------------------------------------------------------------------//
void 
ThreadManager::launch(){
    // Initialize the Logger Instance
    LogManager* LM = LogManager::getInstance();
    
    // Initialize the Data Instance
    DataManager* data = DataManager::getInstance();
     
    // ------ TCP ------ //
    // Launch Clients
    LM->append("Launching Accelerometer Socket\n");
    threadMap[threadKeys::AccelSock] = new thread(&IOManager::clientHandler,this,data->ACCEL);
    
    LM->append("Launching Gyroscope Socket\n");
    threadMap[threadKeys::GyroSock] = new thread(&IOManager::clientHandler,this,data->GYRO);
    
    LM->append("Launching Altitude Socket\n");
    threadMap[threadKeys::AltSock] = new thread(&IOManager::clientHandler,this,data->ALTITUDE);
    
    LM->append("Launching Attitude Socket\n");
    threadMap[threadKeys::AttSock] = new thread(&IOManager::clientHandler,this,data->ATTITUDE);
    
    LM->append("Launching Temperature Socket\n");
    threadMap[threadKeys::TempSock] = new thread(&IOManager::clientHandler,this,data->TEMP);
    
    LM->append("Launching Battery Socket\n");
    threadMap[threadKeys::BatSock] = new thread(&IOManager::clientHandler,this,data->BAT);
    
    // Launch Server
    
    
    // ------ Serial ------ //
    launch_serial();
} //launch()

void
ThreadManager::launch_tcp(){

    
    
    // Launch Server
    
}// launch_tcp













/*
void ThreadManager::launch(IOManager* IO){
    
    // Grab the Log Manager Instance
    LogManager* LM = LogManager::getInstance();
        
    // If we are being launched... the threads better be dead already!
    if(!threadMap.empty()) clean();
    
    // Grab each of the threads, launch 'em and store them back! 
    // TODO: 05May2017 Do an enumeration loop.... There has to be a better way...
    LM->append("|--Launching the TCP Sockets--|\n");
    
    LM->append("Launching Heartbeat Socket\n");
    threadMap[threadKeys::HeartSock] = new thread(&ThreadManager::socketHandler,this,IO,data->HEARTBEAT);
   LM->append("Launching ConfigureData Socket\n");
    threadMap[threadKeys::ConfigSock] = new thread(&ThreadManager::socketHandler,this,IO,data->CONFIG_DATA);
    LM->append("Launching MoveCamera Socket\n");
    threadMap[threadKeys::MoveCamSock] = new thread(&ThreadManager::socketHandler,this,IO,data->MOVE_CAMERA);
    LM->append("Launching Terminate Socket\n");
    threadMap[threadKeys::TerminateSock] = new thread(&ThreadManager::socketHandler,this,IO,data->TERMINATE);
    LM->append("Launching Accelerometer Socket\n");
    threadMap[threadKeys::AccelSock] = new thread(&ThreadManager::socketHandler,this,IO,data->ACCEL);
    LM->append("Launching Gyroscope Socket\n");
    threadMap[threadKeys::GyroSock] = new thread(&ThreadManager::socketHandler,this,IO,data->GYRO);
    LM->append("Launching Altitude Socket\n");
    threadMap[threadKeys::AltSock] = new thread(&ThreadManager::socketHandler,this,IO,data->ALTITUDE);
    LM->append("Launching Attitude Socket\n");
    threadMap[threadKeys::AttSock] = new thread(&ThreadManager::socketHandler,this,IO,data->ATTITUDE);
    LM->append("Launching Temperature Socket\n");
    threadMap[threadKeys::TempSock] = new thread(&ThreadManager::socketHandler,this,IO,data->TEMP);
    LM->append("Launching Battery Socket\n");
    threadMap[threadKeys::BatSock] = new thread(&ThreadManager::socketHandler,this,IO,data->BAT);
    LM->append("Launching the Sensor Thread\n");
    threadMap[threadKeys::SensorKey] = new thread(&ThreadManager::sensorHandler, this, IO);  
} //launch

//----------------------------------------------------------------------------//
// socketHandler() altSock thread
//----------------------------------------------------------------------------//
void ThreadManager::socketHandler(IOManager* IO,int id)
{
    try
    {
        IO->socketHandler(id);
    }catch (...){}
    
} //socketHandler()

//----------------------------------------------------------------------------//
// sensor() sensor thread
//----------------------------------------------------------------------------//
/*void ThreadManager::sensorHandler(IOManager* IO)
{
    try
    {
        IO->launch_sensor();
    }catch (...){}
    
} //serial()
*/


//----------------------------------------------------------------------------//
// clean() cleans up all of the threads
//----------------------------------------------------------------------------//
void 
ThreadManager::clean()
{
    // Initialize the Log Manager
    LogManager* LM = LogManager::getInstance();

    // Initialize the Data Manager
    DataManager* data = DataManager::getInstance();
    
    // Tell all threads to kill themselves
    data->set_threads_timeToDie(true);
    
    // TODO: 05May2017 Do an enumeration loop.... There has to be a better way...
    if(threadMap[threadKeys::HeartSock] != NULL)
    {
        threadMap[threadKeys::HeartSock]->join();
        threadMap.erase(threadKeys::HeartSock);
        LM->append("TCP HeartSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::TerminateSock] != NULL)
    {
        threadMap[threadKeys::TerminateSock]->join();
        threadMap.erase(threadKeys::TerminateSock);
        LM->append("TCP TerminateSock Thread Terminated!\n");
    }
       
    if(threadMap[threadKeys::ConfigSock] != NULL)
    {
        threadMap[threadKeys::ConfigSock]->join();
        threadMap.erase(threadKeys::ConfigSock);
        LM->append("TCP ConfigSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::MoveCamSock] != NULL)
    {
        threadMap[threadKeys::MoveCamSock]->join();
        threadMap.erase(threadKeys::MoveCamSock);
        LM->append("TCP MoveCamSock Thread Terminated!\n");
    }   
    
    if(threadMap[threadKeys::AccelSock] != NULL)
    {
        threadMap[threadKeys::AccelSock]->join();
        threadMap.erase(threadKeys::AccelSock);
        LM->append("TCP AccelSock Thread Terminated!\n");
    }  
    
    if(threadMap[threadKeys::GyroSock] != NULL)
    {
        threadMap[threadKeys::GyroSock]->join();
        threadMap.erase(threadKeys::GyroSock);
        LM->append("TCP GyroSock Thread Terminated!\n");
    }   

    if(threadMap[threadKeys::AltSock] != NULL)
    {
        threadMap[threadKeys::AltSock]->join();
        threadMap.erase(threadKeys::AltSock);
        LM->append("TCP AltSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::AttSock] != NULL)
    {
        threadMap[threadKeys::AttSock]->join();
        threadMap.erase(threadKeys::AttSock);
        LM->append("TCP AttSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::TempSock] != NULL)
    {
        threadMap[threadKeys::TempSock]->join();
        threadMap.erase(threadKeys::TempSock);
        LM->append("TCP TempSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::BatSock] != NULL)
    {
        threadMap[threadKeys::BatSock]->join();
        threadMap.erase(threadKeys::BatSock);
        LM->append("TCP BatSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::SensorKey] != NULL)
    {
        threadMap[threadKeys::SensorKey]->join();
        threadMap.erase(threadKeys::SensorKey);
        LM->append("TCP SensorKey Thread Terminated!\n");
    }
    //LM->append("All Threads Terminated!\n");
} //clean()

//----------------------------------------------------------------------------//
// Constructor
//----------------------------------------------------------------------------//
ThreadManager::ThreadManager() {
    // Grab Our Singleton Instances
    LM = LogManager::getInstance();
    data = DataManager::getInstance();
    WD = WatchDog::getInstance();
}

ThreadManager::ThreadManager(const ThreadManager& orig) {
}

ThreadManager::~ThreadManager() {
    clean();
}

