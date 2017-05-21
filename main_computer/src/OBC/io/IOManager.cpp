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
    LogManager* LM = LogManager::getInstance();
    
    // Grab an instance of the Watchdog
    WatchDog* WD = WatchDog::getInstance();
    
    // Grab the instance of the data manager
    DataManager* data = DataManager::getInstance();
    
    // Initialize the socket health
    data->sockHealth[id] = WD->noFailure;
    
    // Initialize the buffer
    char cbuff[10000];
    string buff;
    
    // Initialize the TCP stream depending on if it is a server or client connection
    if(id >= 200) // then it is a client
    {
        // Initialize the TCP connector
        LM->append("Initializing Connector\n");
        connector = new TCPConnector();
        
        // Keep trying to connect!
        bool connected = false;
        while(!connected){
            stream = connector->connect(HSS_IP.c_str(),PORT_NUMBER+id,CONNECTOR_TIMEOUT);
            // Check for thread interruptions
            usleep(SLEEP_TIME);
            // Tell the WatchDog that you are having trouble connecting!
            if(stream==NULL){
                data->sockHealth[id] = WD->SERVER_CONNECT_FAIL;
            }else{
                connected = true; 
                LM->append("Successful Connect!\n");
            }
            boost::this_thread::interruption_point();
        } //while
        data->sockHealth[id] = WD->socketConnected;
        
    }else{
        // Initialize the TCP acceptor
        LM->append("Initializing Acceptor\n");
        acceptor = new TCPAcceptor(PORT_NUMBER+id,HSS_IP.c_str());   
        
        // Keep trying to accept!
        bool accepted = false;
        while(!accepted){
            stream = acceptor->accept();
            // Check for thread interruptions
            usleep(SLEEP_TIME);
            // Tell the WatchDog that you are having trouble connecting!
            if(stream==NULL){
                data->sockHealth[id] = WD->SERVER_CONNECT_FAIL;
            }else{
                accepted = true; LM->append("Successful Accept\n");
            }
            
            boost::this_thread::interruption_point();
        } //while
        data->sockHealth[id] = WD->socketConnected;
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
                if(stream==NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        if(GM.response().roger_that()) data->HSSAlive = true;
                    } //if
                }//if
                break;
            case data->HEARTBEAT:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        
                        string db;
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        if(GM.heartbeat().alive()){
                            data->HSSAlive = true;
                            LM->append("RECIEVED: HSSAlive == True\n");
                        }else{
                            data->HSSAlive = false;
                            LM->append("RECIEVED: HSSAlive == False\n");
                        }
                    }
                }
                break;
            case data->TERMINATE:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        switch(GM.terminate().terminatekey())
                        {
                            case data->SELF_TERMINATE:
                                LM->append("RECIEVED: SELF TERMINATE COMMAND!\n");
                                timeToDie = true;
                                data->globalShutdown = true;
                                // Cleanup!
                                delete stream;
                                delete acceptor;
                                delete connector;
                                google::protobuf::ShutdownProtobufLibrary();
                                return;
                            case data->SOFT_SHUTDOWN:
                                // TODO 05May2017 att
                               LM->append("RECIEVED: SOFT SHUTDOWN COMMAND!\n");
                               break;
                            case data->EMERGENCY_STOP:
                                //TODO 05May2017 att
                                LM->append("RECIEVED: EMERGENCY STOP COMMAND!\n");
                                break;
                            default: 
                                //TODO 05May2017 att
                                LM->append("Unidentified Terminate Message.\n");
                        }
                    }
                }
                break;
            case data->CONFIG_DATA:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        switch(GM.configdata().configkey())
                        {
                            case data->TOGGLE_ACCEL:
                                LM->append("RECIEVED: Toggle Acceleration!\n");
                                data->sendAccel = !data->sendAccel;
                                break;
                            case data->TOGGLE_GYRO:
                                LM->append("RECIEVED: Toggle Gyro!\n");
                                data->sendGyro = !data->sendGyro;
                                break;
                            case data->TOGGLE_ALTITUDE:
                                LM->append("RECIEVED: Toggle Altitude!\n");
                                data->sendAlt = !data->sendAlt;
                                break;
                            case data->TOGGLE_ATTITUDE:
                                LM->append("RECIEVED: Toggle Attitude!\n");
                                data->sendAtt = !data->sendAtt;
                                break;
                            case data->TOGGLE_TEMP:
                                LM->append("RECIEVED: Toggle Temperature!\n");
                                data->sendTemp = !data->sendTemp;
                                break;
                            case data->TOGGLE_BAT:
                                LM->append("RECIEVED: Toggle Bat!\n");
                                data->sendBat = !data->sendBat;
                                break;
                            default:
                                LM->append("Unidentified ConfigData Message.\n");
                                break;
                        } // switch      
                    } // if
                } // else
                break;
            case data->MOVE_CAMERA:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if(stream==NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else{
                    if (stream->receive(cbuff,sizeof(cbuff),ACCEPTOR_TIMEOUT) > 0)
                    {
                        GM.ParseFromArray(cbuff,sizeof(cbuff));
                        switch(GM.movecamera().arrowkey())
                        {
                            case 0:
                                LM->append("RECIEVED: Move Camera Up Command!\n");
                                break;
                            case 1:
                                LM->append("RECIEVED: Move Camera Right Command!\n");                                                                break;
                            case data->TOGGLE_ALTITUDE:
                                LM->append("RECIEVED: Move Camera Down Command!\n");
                                break;
                            case data->TOGGLE_ATTITUDE:
                                LM->append("RECIEVED: Move Camera Left Command!\n");
                                break;
                            default:
                                LM->append("Unidentified Move Camera Command.\n");
                                break;
                        } // switch      
                    } // if
                } // else
                break;
            case data->ACCEL:
                if(data->sendAccel){
                    if(!data->accelQueue.isEmpty()){
                        // grab the data and remove it from the queue
                        vector<double> accel = data->accelQueue.front();
                        data->accelQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) accel[0]); 
                        GM.mutable_accel()->set_x(accel[1]);
                        GM.mutable_accel()->set_y(accel[2]);
                        GM.mutable_accel()->set_z(accel[3]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff); 
                        stream->send(buff.c_str(),buff.length());
                        buff.clear();
                    } // if accelQueue is not empty
                } // if sendAccel
                break;
            case data->GYRO:
                if(data->sendGyro){
                    if(!data->gyroQueue.isEmpty()){
                        // grab the data and remove it from the queue
                        vector<double> gyro = data->gyroQueue.front();
                        data->gyroQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) gyro[0]); 
                        GM.mutable_gyro()->set_x(gyro[1]);
                        GM.mutable_gyro()->set_y(gyro[2]);
                        GM.mutable_gyro()->set_z(gyro[3]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff); 
                        stream->send(buff.c_str(),buff.length());
                        buff.clear();
                    } // if gyroQueue is not empty
                } // if sendGyro
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
                    } // if altitude queue is not empty
                } // if send alt
                break;
            case data->TEMP:
                if(data->sendTemp){
                    // if there is data, send it!
                    if(!data->tempQueue.isEmpty()){
                        // grab the data and remove it from the queue
                        vector<double> temp = data->tempQueue.front();
                        data->tempQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) temp[0]); 
                        GM.mutable_temp()->set_temp(temp[1]); 
                        // Serialize and send it!
                        GM.SerializeToString(&buff); 
                        stream->send(buff.c_str(),buff.length());
                        buff.clear();
                    } // if altitude queue is not empty
                } // if sendTemp
                break;
            case data->BAT:
                if(data->sendBat){
                    // if there is data, send it!
                    if(!data->batteryQueue.isEmpty()){
                        // grab the data and remove it from the queue
                        vector<double> bat = data->batteryQueue.front();
                        data->batteryQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) bat[0]); 
                        GM.mutable_bat()->set_percent(bat[1]); 
                        // Serialize and send it!
                        GM.SerializeToString(&buff); 
                        stream->send(buff.c_str(),buff.length());
                        buff.clear();
                    } // if altitude queue is not empty
                }// if sendTemp
                break;
            default:
                data->sockHealth[id] = WD->UNK_SOCK;
                // Cleanup!
                delete stream;
                delete acceptor;
                delete connector;
                
                return;
        }
        // Check for thread interruptions
        boost::this_thread::interruption_point();
    } //while(!timeToDie)
    
    // Cleanup!
    delete stream;
    delete acceptor;
    delete connector;
} // socketHandler()

IOManager::IOManager() {}

IOManager::IOManager(const IOManager& orig) {}

IOManager::~IOManager() {
    clean();
}

