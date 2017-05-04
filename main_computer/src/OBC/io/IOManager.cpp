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
 * File:   InputOutput.cpp
 * Author: controlxfreak
 * 
 * Created on April 7, 2017, 11:23 AM
 */

#include "IOManager.h"

//----------------------------------------------------------------------------//
// socketHandler() launches and runs a socket thread until the timeToDie 
//              flag is set or it is interrupted by the thread manager
//----------------------------------------------------------------------------//
void IOManager::socketHandler(int id){
    
    // Verify that the version of the library that we linked against is
    // compatible with the version of the headers we compiled against.
    GOOGLE_PROTOBUF_VERIFY_VERSION;
    
    // Initialize the TCP Classes
    TCPStream* stream;
    TCPAcceptor* acceptor;
    TCPConnector* connector;
        
    // Initialize the Message
    T7::GenericMessage GM;
        
    // Grab an instance of the LogManager
    LM = LogManager::getInstance();
    
    // Grab an instance of the Watchdog
    WD = WatchDog::getInstance();
    
    // Initialize the buffer
    char cbuff[256];
    string buff;
    
    // Initialize the TCP stream depending on if it is a server or client connection
    if(id >= 200) // then it is a client
    {
        // Initialize the TCP connector
        connector = new TCPConnector(id);
        
        // Keep trying to connect!
        while(stream==NULL ){
            stream = connector->connect(HSS_IP.c_str(),PORT_NUMBER,CONNECTOR_TIMEOUT);
            // Check for thread interruptions
            usleep(SLEEP_TIME);
            boost::this_thread::interruption_point();
        } //while
        
        // Tell the WatchDog that you are having trouble connecting!
        if(stream==NULL) WD->addTCPError(WD->SERVER_CONNECT,id);
    }else{
        // Initialize the TCP acceptor
        acceptor = new TCPAcceptor(PORT_NUMBER,HSS_IP.c_str(),id);   
        
        // Keep trying to accept!
        while(stream==NULL ){
            stream = acceptor->accept();
            // Check for thread interruptions
            usleep(SLEEP_TIME);
            boost::this_thread::interruption_point();
        } //while
        
        // Tell the WatchDog that you are having trouble connecting!
        if(stream==NULL) WD->addTCPError(WD->SERVER_CONNECT,id);
    } //else
    
    // Loop through until you we're done!
    while(!timeToDie)
    {
        // Clear the generic message for this round
        GM.Clear();
        
        // Switch based on the sock id
        switch(id){
            case data->RESPONSE: 
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) WD->addTCPError(WD->TCP_ACCEPT,id);
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        if(GM.response().roger_that()) data->HSSAlive = true;
                    } //if
                }//if
            case data->HEARTBEAT:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) WD->addTCPError(WD->TCP_ACCEPT,id);
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        if(GM.heartbeat().alive())
                            data->HSSAlive = true;
                        else
                            data->HSSAlive = false;
                    }
                }
            case data->TERMINATE:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) WD->addTCPError(WD->TCP_ACCEPT,id);
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        if(GM.terminate().terminate())
                        {
                            timeToDie = true;
                            data->globalShutdown = true;
                        }                        
                    }
                }
            case data->UPDATE_PARAM:
                break;
            case data->CONFIG_DATA:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) WD->addTCPError(WD->TCP_ACCEPT,id);
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        switch(GM.configdata().configkey())
                        {
                            case GM.configdata().toggleAccel:
                                LM->append("Toggle Acceleration Message Received!\n");
                                data->sendAccel = !data->sendAccel;
                            case GM.configdata().toggleGyro:
                                LM->append("Toggle Gyro Message Received!\n");
                                data->sendGyro = !data->sendGyro;
                            case GM.configdata().toggleAltitude:
                            case GM.configdata().toggleAttitude:
                            case GM.configdata().toggleTemp:
                            case GM.configdata().toggleBat:
                            default:
                                LM->append("Unidentified ConfigData Message.\n");
                        }
                        timeToDie = true;
                        data->globalShutdown = true;               
                    }
                }
                break;
            case data->MOVE_CAMERA:
                break;
            case data->ACCEL:
                if(data->sendAccel){}
            case data->GYRO:
                if(data->sendGyro){}
                break;
            case data->ALTITUDE:
                if(data->sendAlt)
                {
                    // if there is data, send it!
                    if(!data->altitudeQueue.isEmpty()){
                        // grab the data and remove it from the queue
                        vector<double> alt = data->altitudeQueue.front();
                        data->altitudeQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) alt[0]); 
                        GM.mutable_altitude()->set_alt(alt[1]); 
                        // Serialize and send it!
                        GM.SerializeToString(&buff); 
                        stream->send(buff.c_str(),buff.length());
                        buff.clear();
                    }
                }
            case data->TEMP:
                if(data->sendTemp){}
            case data->BAT:
                if(data->sendTemp){}
            default:
                WD->addTCPError(WD->UNKN_SOCK,id);
                // Cleanup!
                delete stream;
                google::protobuf::ShutdownProtobufLibrary();
                return;
        }
        // Check for thread interruptions
        boost::this_thread::interruption_point();
    } //while(!ttd)
    
    // Cleanup!
    delete stream;
    google::protobuf::ShutdownProtobufLibrary();
}

IOManager::IOManager() {
    data = DataManager::getInstance();
}

IOManager::IOManager(const IOManager& orig) {
}

IOManager::~IOManager() {
    clean();
}

