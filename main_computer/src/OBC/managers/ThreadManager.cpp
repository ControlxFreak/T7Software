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
 * File:   ThreadManager.cpp
 * Author: controlxfreak
 * 
 * Created on April 7, 2017, 11:30 AM
 */

#include "ThreadManager.h"

//----------------------------------------------------------------------------//
// launch() launches all of the threads
//----------------------------------------------------------------------------//
void ThreadManager::launch(IOManager* IO){
    
    // Grab the Log Manager Instance
    LogManager* LM = LogManager::getInstance();
    
    // If we are being launched... the threads better be dead already!
    if(!threadMap.empty()) clean();
    
    // Grab each of the threads, launch 'em and store them back! 
    // TODO: 05May2017 Do an enumeration loop.... There has to be a better way...
    LM->append("|--Launching the TCP Sockets--|\n");
    LM->append("Launching Terminate Socket\n");
    threadMap[threadKeys::TerminateSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->TERMINATE);
   /*
     LM->append("Launching Heartbeat Socket\n");
    threadMap[threadKeys::HeartSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->HEARTBEAT);
    LM->append("Launching Terminate Socket\n");
    threadMap[threadKeys::TerminateSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->TERMINATE);
    LM->append("Launching ConfigureData Socket\n");
    threadMap[threadKeys::ConfigSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->CONFIG_DATA);
    LM->append("Launching MoveCamera Socket\n");
    threadMap[threadKeys::MoveCamSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->MOVE_CAMERA);
    LM->append("Launching Accelerometer Socket\n");
    threadMap[threadKeys::AccelSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->ACCEL);
    LM->append("Launching Gyroscope Socket\n");
    threadMap[threadKeys::GyroSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->GYRO);
    LM->append("Launching Altitude Socket\n");
    threadMap[threadKeys::AltSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->ALTITUDE);
    LM->append("Launching Attitude Socket\n");
    threadMap[threadKeys::AttSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->ATTITUDE);
    LM->append("Launching Temperature Socket\n");
    threadMap[threadKeys::TempSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->TEMP);
    LM->append("Launching Battery Socket\n");
    threadMap[threadKeys::BatSock] = new boost::thread(&ThreadManager::socketHandler,this,IO,data->BAT);
    
    LM->append("Launching the Sensor Thread\n");
    threadMap[threadKeys::SensorKey] = new boost::thread(&ThreadManager::sensorHandler, this, IO);  
    */
} //launch

//----------------------------------------------------------------------------//
// socketHandler() altSock thread
//----------------------------------------------------------------------------//
void ThreadManager::socketHandler(IOManager* IO,int id)
{
    try
    {
        IO->socketHandler(id);
    }catch (boost::thread_interrupted&){}
    
} //socketHandler()

//----------------------------------------------------------------------------//
// sensor() sensor thread
//----------------------------------------------------------------------------//
void ThreadManager::sensorHandler(IOManager* IO)
{
    try
    {
        IO->launch_sensor();
    }catch (boost::thread_interrupted&){}
    
} //serial()

//----------------------------------------------------------------------------//
// clean() cleans up all of the threads
//----------------------------------------------------------------------------//
void ThreadManager::clean()
{
    // Initialize the Log Manager
    LogManager* LM = LogManager::getInstance();

    // TODO: 05May2017 Do an enumeration loop.... There has to be a better way...
    if(threadMap[threadKeys::HeartSock] != NULL)
    {
        threadMap[threadKeys::HeartSock]->interrupt();
        threadMap[threadKeys::HeartSock]->join();
        threadMap.erase(threadKeys::HeartSock);
        LM->append("TCP HeartSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::TerminateSock] != NULL)
    {
        threadMap[threadKeys::TerminateSock]->interrupt();
        threadMap[threadKeys::TerminateSock]->join();
        threadMap.erase(threadKeys::TerminateSock);
        LM->append("TCP TerminateSock Thread Terminated!\n");
    }
       
    if(threadMap[threadKeys::ConfigSock] != NULL)
    {
        threadMap[threadKeys::ConfigSock]->interrupt();
        threadMap[threadKeys::ConfigSock]->join();
        threadMap.erase(threadKeys::ConfigSock);
        LM->append("TCP ConfigSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::MoveCamSock] != NULL)
    {
        threadMap[threadKeys::MoveCamSock]->interrupt();
        threadMap[threadKeys::MoveCamSock]->join();
        threadMap.erase(threadKeys::MoveCamSock);
        LM->append("TCP MoveCamSock Thread Terminated!\n");
    }   
    
    if(threadMap[threadKeys::AccelSock] != NULL)
    {
        threadMap[threadKeys::AccelSock]->interrupt();
        threadMap[threadKeys::AccelSock]->join();
        threadMap.erase(threadKeys::AccelSock);
        LM->append("TCP AccelSock Thread Terminated!\n");
    }  
    
    if(threadMap[threadKeys::GyroSock] != NULL)
    {
        threadMap[threadKeys::GyroSock]->interrupt();
        threadMap[threadKeys::GyroSock]->join();
        threadMap.erase(threadKeys::GyroSock);
        LM->append("TCP GyroSock Thread Terminated!\n");
    }   

    if(threadMap[threadKeys::AltSock] != NULL)
    {
        threadMap[threadKeys::AltSock]->interrupt();
        threadMap[threadKeys::AltSock]->join();
        threadMap.erase(threadKeys::AltSock);
        LM->append("TCP AltSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::AttSock] != NULL)
    {
        threadMap[threadKeys::AttSock]->interrupt();
        threadMap[threadKeys::AttSock]->join();
        threadMap.erase(threadKeys::AttSock);
        LM->append("TCP AttSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::TempSock] != NULL)
    {
        threadMap[threadKeys::TempSock]->interrupt();
        threadMap[threadKeys::TempSock]->join();
        threadMap.erase(threadKeys::TempSock);
        LM->append("TCP TempSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::BatSock] != NULL)
    {
        threadMap[threadKeys::BatSock]->interrupt();
        threadMap[threadKeys::BatSock]->join();
        threadMap.erase(threadKeys::BatSock);
        LM->append("TCP BatSock Thread Terminated!\n");
    }
    
    if(threadMap[threadKeys::SensorKey] != NULL)
    {
        threadMap[threadKeys::SensorKey]->interrupt();
        threadMap[threadKeys::SensorKey]->join();
        threadMap.erase(threadKeys::SensorKey);
        LM->append("TCP SensorKey Thread Terminated!\n");
    }
    
    LM->append("All Threads Terminated!\n");
    
} //clean()

//----------------------------------------------------------------------------//
// Constructor
//----------------------------------------------------------------------------//
ThreadManager::ThreadManager() {
}

ThreadManager::ThreadManager(const ThreadManager& orig) {
}

ThreadManager::~ThreadManager() {
    clean();
}

