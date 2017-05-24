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
    21 May 2017 - t3 - re-write
--------------------------------------------------------------------------------
 */

#include "IOManager.h"

//----------------------------------------------------------------------------//
// launch() launches the tcp and serial IO communications
//----------------------------------------------------------------------------//

void
IOManager::launch() {

    // Launch the clients
    launch_clients();

    // launch the server
    launch_server();

    // launch the serial
    launch_serial();

}//launch()

//----------------------------------------------------------------------------//
// launch_clients() launches the tcp clients
//----------------------------------------------------------------------------//

void
IOManager::launch_clients() {

    LM->append("Launching Accelerometer Socket\n");
    sockThreadMap[AccelSock] = new thread(&IOManager::client_handler, this, data->ACCEL);

    /*
        LM->append("Launching Gyroscope Socket\n");
        sockThreadMap[GyroSock] = new thread(&IOManager::client_handler, this, data->GYRO);

        LM->append("Launching Altitude Socket\n");
        sockThreadMap[AltSock] = new thread(&IOManager::client_handler, this, data->ALTITUDE);

        LM->append("Launching Attitude Socket\n");
        sockThreadMap[AttSock] = new thread(&IOManager::client_handler, this, data->ATTITUDE);

        LM->append("Launching Temperature Socket\n");
        sockThreadMap[TempSock] = new thread(&IOManager::client_handler, this, data->TEMP);

        LM->append("Launching Battery Socket\n");
        sockThreadMap[BatSock] = new thread(&IOManager::client_handler, this, data->BAT);
     */
}//launch_clients()

//----------------------------------------------------------------------------//
// launch_server() launches the tcp server
//----------------------------------------------------------------------------//

void
IOManager::launch_server() {

    LM->append("Launching Server Socket\n");
    sockThreadMap[ServerSock] = new thread(&IOManager::server_handler, this);

}//launch_server()

//----------------------------------------------------------------------------//
// launch_serial() launches the serial thread
//----------------------------------------------------------------------------//

void
IOManager::launch_serial() {

    LM->append("Launching Serial Communication\n");

}//launch_server()

//----------------------------------------------------------------------------//
// client_handler() handles all tcp client communication
//----------------------------------------------------------------------------//

void
IOManager::client_handler(int id) {

    // Verify that the version of the library that we linked against is
    // compatible with the version of the headers we compiled against.
    GOOGLE_PROTOBUF_VERIFY_VERSION;

    // Initialize the Message
    T7::GenericMessage GM;

    // Initialize the socket health
    data->sockHealth[id] = WD->noFailure;

    // Initialize the TCP Classes
    TCPStream* stream;
    TCPConnector* connector;
    connector = new TCPConnector();

    // Ignore broken pipes... I'll handle it myself.
    signal(SIGPIPE, SIG_IGN);

    // Initialize the buffer
    string buff;
    char cbuff[256];

    // Initialize return codes
    bool connected = false;
    int rc;
    int tryNum = 0;

    // Initialize the TCP connector
    LM->append("Initializing Connector\n");
    while (!connected && !data->timeToDieMap[id]) {

        stream = connector->connect(HSS_IP.c_str(), CLIENT_PORT_NUMBER);
        if (stream == NULL) {
            if (tryNum > 1E6) {
                LM->append("Waiting to Connect to Server\n");
                tryNum = 0;
            } else {
                tryNum++;
            };
        } else {
            connected = true;
            LM->append("Successful Connect!\n");
            data->sockHealth[id] = WD->socketConnected;
        } // if
    } //while

    while (!data->timeToDieMap[id]) {
        switch (id) {
            case data->ACCEL:
                if (data->sendAccel) {
                    if (!data->accelQueue.isEmpty()) {
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
                        rc = stream->send(buff.c_str(), buff.length());

                        if (rc < 0 || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = WD->SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if accelQueue is not empty
                } // if sendAccel
                break;

            case data->GYRO:
                if (data->sendGyro) {
                    if (!data->gyroQueue.isEmpty()) {
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
                        rc = stream->send(buff.c_str(), buff.length());

                        if (rc < 0 || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = WD->SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if gyroQueue is not empty
                } // if sendGyro
                break;
            case data->ALTITUDE:
                if (data->sendAlt) {
                    // if there is data, send it!
                    if (!data->altitudeQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> alt = data->altitudeQueue.front();
                        data->altitudeQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) alt[0]);
                        GM.mutable_altitude()->set_alt(alt[1]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff);
                        rc = stream->send(buff.c_str(), buff.length());

                        if (rc < 0 || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = WD->SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if altitude queue is not empty
                } // if send alt
                break;
            case data->TEMP:
                if (data->sendTemp) {
                    // if there is data, send it!
                    if (!data->tempQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> temp = data->tempQueue.front();
                        data->tempQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) temp[0]);
                        GM.mutable_temp()->set_temp(temp[1]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff);
                        rc = stream->send(buff.c_str(), buff.length());

                        if (rc < 0 || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = WD->SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if altitude queue is not empty
                } // if sendTemp
                break;
            case data->BAT:
                if (data->sendBat) {
                    // if there is data, send it!
                    if (!data->batteryQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> bat = data->batteryQueue.front();
                        data->batteryQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) bat[0]);
                        GM.mutable_bat()->set_percent(bat[1]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff);
                        rc = stream->send(buff.c_str(), buff.length());

                        if (rc < 0 || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = WD->SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if altitude queue is not empty
                }// if sendTemp
                break;
            default:
                data->sockHealth[id] = WD->UNK_SOCK;
                data->timeToDieMap[id] = true;
        } // switch
    } // while(!timeToDie)
    delete connector;
    data->sockHealth[id] = WD->socketDisconnected;
    return;
}//launch_client()


//----------------------------------------------------------------------------//
// server_handler() handles all tcp client communication
//----------------------------------------------------------------------------//

void
IOManager::server_handler() {

    // Initialize the TCP Classes
    TCPStream* stream;
    TCPAcceptor* acceptor;
    acceptor = new TCPAcceptor(SERVER_PORT_NUMBER, HSS_IP.c_str());

    // Initialize the Message
    T7::GenericMessage GM;

    // Initialize the buffer
    char cbuff[256];
    char pbuff[256];
    int id = -1;

    // Initialize the TCP connector
    LM->append("Initializing Server\n");

    while (!data->timeToDieMap[data->SERVER_SHUTDOWN]) {
        if (acceptor->start() == 0) {
            stream = acceptor->accept();
            // Tell the WatchDog that you are having trouble connecting!
            if (stream != NULL) {
                LM->append("Successful Accept\n");
                if (stream->receive(cbuff, sizeof (cbuff), CLIENT_TIMEOUT) > 0) {
                    GM.ParseFromArray(cbuff, sizeof (cbuff));
                    id = (int) GM.msgtype();

                    // ONLY RE LAUNCH IF THE CURRENT ONE IS DEAD!
                    if (sockThreadMap[id] == NULL || data->sockHealth[id] == WD->socketDisconnected) {
                        snprintf(pbuff, 256, "Launching Acceptor for Socket %d!\n", id);
                        string sbuff(pbuff);
                        LM->append(sbuff);
                        sockThreadMap[id] = new thread(&IOManager::acceptor_handler, this, stream, id);
                    }
                    stream = NULL;
                } else {
                    LM->append("Unsuccessful Receive.");
                } //if receive
            } // if steam
        } // if start
    } //while
} //server_handler

//----------------------------------------------------------------------------//
// acceptor_handler deals with an acceptor and continuously reads from the stream
//----------------------------------------------------------------------------//

void
IOManager::acceptor_handler(TCPStream* stream, int id) {

    // Initialize the Message
    T7::GenericMessage GM;

    // Initialize the buffer
    char cbuff[256];
    char pbuff[256];
    int rc;

    // Initialize the socket health
    data->sockHealth[id] = WD->socketConnected;
    data->timeToDieMap[id] = false;

    while (!data->timeToDieMap[id]) {
        switch (id) {
            case data->HEARTBEAT:
                rc = stream->receive(cbuff, sizeof (cbuff), CLIENT_TIMEOUT);
                if (rc > 0) {
                    GM.ParseFromArray(cbuff, sizeof (cbuff));
                    if ((int) GM.msgtype() == id) {
                        if (GM.heartbeat().alive()) {
                            WD->HSSAlive = true;
                            LM->append("RECIEVED: HSSAlive == True\n");
                        } else {
                            WD->HSSAlive = false;
                            LM->append("RECIEVED: HSSAlive == False\n");
                        } // alive
                    } else {
                        snprintf(pbuff, 256, "CONTAMINATED STREAM!  ID = %d, RECEIVED = %d!\n", id, GM.msgtype());
                        string sbuff(pbuff);
                        LM->append(sbuff);
                        data->timeToDieMap[id] = true;
                    }//id check
                } else {
                    snprintf(pbuff, 256, "Dead Stream!  ID = %d\n", id);
                    string sbuff(pbuff);
                    LM->append(sbuff);
                    data->timeToDieMap[id] = true;
                }
                break;
            case data->TERMINATE:
                if (stream->receive(cbuff, sizeof (cbuff), CLIENT_TIMEOUT) > 0) {
                    GM.ParseFromArray(cbuff, sizeof (cbuff));
                    if ((int) GM.msgtype() == id) {
                        switch (GM.terminate().terminatekey()) {
                            case data->SELF_TERMINATE:
                                LM->append("RECIEVED: SELF TERMINATE COMMAND!\n");
                                data->timeToDieMap[id] = true;
                                data->timeToDieMap[data->GLOBAL_SHUTDOWN] = true;
                                WD->SystemHealth = WD->RESTART;
                                break;
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
                        } //switch
                    } else {
                        snprintf(pbuff, 256, "CONTAMINATED STREAM!  ID = %d, RECEIVED = %d!\n", id, GM.msgtype());
                        string sbuff(pbuff);
                        LM->append(sbuff);
                        data->timeToDieMap[id] = true;
                    }//id check
                } //if stream
                break;
                case data->CONFIG_DATA:
                    rc = stream->receive(cbuff, sizeof (cbuff), CLIENT_TIMEOUT);
                    if (rc > 0) {
                            GM.ParseFromArray(cbuff, sizeof (cbuff));
                            switch (GM.configdata().configkey()) {
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
                    break;
                case data->MOVE_CAMERA:
                    rc = stream->receive(cbuff, sizeof (cbuff), CLIENT_TIMEOUT);
                    if (rc > 0) {
                            GM.ParseFromArray(cbuff, sizeof (cbuff));
                            switch (GM.movecamera().arrowkey()) {
                                case 0:
                                    LM->append("RECIEVED: Move Camera Up Command!\n");
                                    break;
                                case 1:
                                    LM->append("RECIEVED: Move Camera Right Command!\n");
                                    break;
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
                    break;
            default:
                data->sockHealth[id] = WD->UNK_SOCK;
                data->timeToDieMap[id] = true;
        } //switch
    } // while

    data->sockHealth[id] = WD->socketDisconnected;
    delete stream;

    return;
} // acceptor_handler


//----------------------------------------------------------------------------//
// socketHandler() launches and runs a socket thread until the timeToDie 
//              flag is set or it is interrupted by the thread manager
//----------------------------------------------------------------------------//
/*
void IOManager::socketHandler(int id) {

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
    if (id >= 200) // then it is a client
    {
        // Initialize the TCP connector
        LM->append("Initializing Connector\n");
        connector = new TCPConnector();

        // Keep trying to connect!
        bool connected = false;
        while (!connected && !data->globalShutdown) {
            stream = connector->connect(HSS_IP.c_str(), PORT_NUMBER + id, CONNECTOR_TIMEOUT);
            // Check for thread interruptions
            usleep(SLEEP_TIME);
            // Tell the WatchDog that you are having trouble connecting!
            if (stream == NULL) {
                data->sockHealth[id] = WD->SERVER_CONNECT_FAIL;
            } else {
                connected = true;
                LM->append("Successful Connect!\n");
            }
        } //while
        data->sockHealth[id] = WD->socketConnected;

    } else {
        // Initialize the TCP acceptor
        LM->append("Initializing Acceptor\n");
        acceptor = new TCPAcceptor(PORT_NUMBER + id, HSS_IP.c_str());

        // Keep trying to accept!
        bool accepted = false;
        while (!accepted && !(data->globalShutdown)) {
            stream = acceptor->accept();
            // Check for thread interruptions
            usleep(SLEEP_TIME);
            // Tell the WatchDog that you are having trouble connecting!
            if (stream == NULL) {
                data->sockHealth[id] = WD->SERVER_CONNECT_FAIL;
            } else {
                accepted = true;
                LM->append("Successful Accept\n");
            }

        } //while
        data->sockHealth[id] = WD->socketConnected;
    } //else

    // Loop through until you we're done!
    while (!timeToDie && !data->globalShutdown) {
        // Clear the generic message for this round
        GM.Clear();

        // Switch based on the sock id
        switch (id) {
            case data->RESPONSE:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if (stream == NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else {
                    if (stream->receive(cbuff, sizeof (cbuff), ACCEPTOR_TIMEOUT) > 0) {
                        GM.ParseFromArray(cbuff, sizeof (cbuff));
                        if (GM.response().roger_that()) data->HSSAlive = true;
                    } //if
                }//if
                break;
            case data->HEARTBEAT:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if (stream == NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else {
                    if (stream->receive(cbuff, sizeof (cbuff), ACCEPTOR_TIMEOUT) > 0) {

                        string db;
                        GM.ParseFromArray(cbuff, sizeof (cbuff));
                        if (GM.heartbeat().alive()) {
                            data->HSSAlive = true;
                            LM->append("RECIEVED: HSSAlive == True\n");
                        } else {
                            data->HSSAlive = false;
                            LM->append("RECIEVED: HSSAlive == False\n");
                        }
                    }
                }
                break;
            case data->TERMINATE:
                stream = acceptor->accept();
                // Tell the WatchDog that you are having trouble connecting!
                if (stream == NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else {
                    if (stream->receive(cbuff, sizeof (cbuff), ACCEPTOR_TIMEOUT) > 0) {
                        GM.ParseFromArray(cbuff, sizeof (cbuff));
                        switch (GM.terminate().terminatekey()) {
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
                if (stream == NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else {
                    if (stream->receive(cbuff, sizeof (cbuff), ACCEPTOR_TIMEOUT) > 0) {
                        GM.ParseFromArray(cbuff, sizeof (cbuff));
                        switch (GM.configdata().configkey()) {
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
                if (stream == NULL) data->sockHealth[id] = WD->SERVER_ACCEPT_FAIL;
                else {
                    if (stream->receive(cbuff, sizeof (cbuff), ACCEPTOR_TIMEOUT) > 0) {
                        GM.ParseFromArray(cbuff, sizeof (cbuff));
                        switch (GM.movecamera().arrowkey()) {
                            case 0:
                                LM->append("RECIEVED: Move Camera Up Command!\n");
                                break;
                            case 1:
                                LM->append("RECIEVED: Move Camera Right Command!\n");
                                break;
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
                if (data->sendAccel) {
                    if (!data->accelQueue.isEmpty()) {
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
                        stream->send(buff.c_str(), buff.length());
                        buff.clear();
                    } // if accelQueue is not empty
                } // if sendAccel
                break;
            case data->GYRO:
                if (data->sendGyro) {
                    if (!data->gyroQueue.isEmpty()) {
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
                        stream->send(buff.c_str(), buff.length());
                        buff.clear();
                    } // if gyroQueue is not empty
                } // if sendGyro
                break;
            case data->ALTITUDE:
                if (data->sendAlt) {
                    // if there is data, send it!
                    if (!data->altitudeQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> alt = data->altitudeQueue.front();
                        data->altitudeQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) alt[0]);
                        GM.mutable_altitude()->set_alt(alt[1]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff);
                        stream->send(buff.c_str(), buff.length());
                        buff.clear();
                    } // if altitude queue is not empty
                } // if send alt
                break;
            case data->TEMP:
                if (data->sendTemp) {
                    // if there is data, send it!
                    if (!data->tempQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> temp = data->tempQueue.front();
                        data->tempQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) temp[0]);
                        GM.mutable_temp()->set_temp(temp[1]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff);
                        stream->send(buff.c_str(), buff.length());
                        buff.clear();
                    } // if altitude queue is not empty
                } // if sendTemp
                break;
            case data->BAT:
                if (data->sendBat) {
                    // if there is data, send it!
                    if (!data->batteryQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> bat = data->batteryQueue.front();
                        data->batteryQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) bat[0]);
                        GM.mutable_bat()->set_percent(bat[1]);
                        // Serialize and send it!
                        GM.SerializeToString(&buff);
                        stream->send(buff.c_str(), buff.length());
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
    } //while(!timeToDie)

    // Cleanup!
    delete stream;
    delete acceptor;
    delete connector;
} // socketHandler()
 */
//----------------------------------------------------------------------------//
// socketHandler() launches and runs a socket thread until the timeToDie 
//              flag is set or it is interrupted by the thread manager
//----------------------------------------------------------------------------//

/*
void
IOManager::clientHandler(int id) {

    // Verify that the version of the library that we linked against is
    // compatible with the version of the headers we compiled against.
    GOOGLE_PROTOBUF_VERIFY_VERSION;

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
    string buff;

    try {
        // Initialize the socket
        ClientSocket client_socket(HSS_IP, CLIENT_PORT_NUMBER);

        // try to send the message
        while (!timeToDie) {
            try {
                switch (id) {
                    case data->ACCEL:
                        if (data->sendAccel) {
                            if (!data->accelQueue.isEmpty()) {
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
                                client_socket << buff;
                                buff.clear();
                            } // if accelQueue is not empty
                        } // if sendAccel
                        break;
                    default:
                        data->sockHealth[id] = WD->UNK_SOCK;
                        return;
                } // switch
            } catch (SocketException& e) {
                char cbuff[200];
                snprintf(cbuff, 200, "Socket %d Failure.  Exception: %s", id, e.description().c_str());
                string sbuff(cbuff);
                LM->append(sbuff);
                data->sockHealth[id] = WD->SOCKET_FAILURE;
            } // catch
        } // while(!timeToDie)
    } catch (SocketException& e) {
        char cbuff[200];
        snprintf(cbuff, 200, "Socket %d Failure.  Exception: %s", id, e.description().c_str());
        string sbuff(cbuff);
        LM->append(sbuff);
        data->sockHealth[id] = WD->SOCKET_FAILURE;
        return;
    }//catch(SocketException&)
    return;
} // clientHandler
 */

IOManager::IOManager() {
    // Initialize the Logger Instance
    LM = LogManager::getInstance();

    // Initialize the Data Instance
    data = DataManager::getInstance();

    // Initialize the Data Instance
    WD = WatchDog::getInstance();
}

IOManager::IOManager(const IOManager & orig) {
}

IOManager::~IOManager() {
    clean();
}

