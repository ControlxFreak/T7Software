
/*------------------------------------------------------------------------------
Function Name: IOManager.cpp

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
    sockThreadMap[AccelSock] = new thread(&IOManager::client_handler, this, sockKeys::ACCEL);

    LM->append("Launching Gyroscope Socket\n");
    sockThreadMap[GyroSock] = new thread(&IOManager::client_handler, this, sockKeys::GYRO);

    LM->append("Launching Altitude Socket\n");
    sockThreadMap[AltSock] = new thread(&IOManager::client_handler, this, sockKeys::ALTITUDE);

    LM->append("Launching Attitude Socket\n");
    sockThreadMap[AttSock] = new thread(&IOManager::client_handler, this, sockKeys::ATTITUDE);

    LM->append("Launching Temperature Socket\n");
    sockThreadMap[TempSock] = new thread(&IOManager::client_handler, this, sockKeys::TEMP);

    LM->append("Launching Battery Socket\n");
    sockThreadMap[BatSock] = new thread(&IOManager::client_handler, this, sockKeys::BAT);

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

    LM->append("Launching Wifi Handler\n");
    sockThreadMap[WiFiKey] = new thread(&IOManager::wifi_handler, this);

    LM->append("Launching Thermal Array Handler\n");
    sockThreadMap[ThermalArrayKey] = new thread(&IOManager::thermal_array_handler, this);

    /*LM->append("Launching Pixhawk Handler\n");
    sockThreadMap[PixhawkKey] = new thread(&IOManager::pixhawk_handler, this);
    */
}//launch_serial()

// -------------------------------------------------------------------------- // 
// pixhawk_handler() handles all pixhawk stuffz.
// -------------------------------------------------------------------------- // 
void
IOManager::pixhawk_handler(){

  return;
}


// -------------------------------------------------------------------------- //
// thermal_array_handler() handles all thermal array magic
// -------------------------------------------------------------------------- // 
void
IOManager::thermal_array_handler(){
	LM->append("Thermal Array Stuff Launched!\n");
	return;
}


// --------------------------------------------------------------------------- //
// wifi_handler() handles all wifi sniffing
// --------------------------------------------------------------------------- //
void
IOManager::wifi_handler(){
	//struct to hold collected information
	struct sig_info {
	    char mac[18];
	    char ssid[33];
	    int bitrate;
	    int level;
	};

	sig_info sigInfo = {"0","0",0,0};
	iwreq req;
	strcpy(req.ifr_name,"wlan0");
	iw_statistics *stats;
	// create a ioctl socket!
	int sockfd = socket(AF_INET,SOCK_DGRAM,0);
	
	//make room for the iw_statistics object
    	req.u.data.pointer = (iw_statistics *)malloc(sizeof(iw_statistics));
    	req.u.data.length = sizeof(iw_statistics);
	
	vector< double > sl;
	while(!data->timeToDieMap[WIFI_SHUTDOWN]){
	        //this will gather the signal strength
	        if(ioctl(sockfd, SIOCGIWSTATS, &req) == -1){
	            //die with error, invalid interface
	            fprintf(stderr, "Invalid interface.\n");
		    return;
	        }else if(((iw_statistics *)req.u.data.pointer)->qual.updated & IW_QUAL_DBM){
	            //signal is measured in dBm and is valid for us to use
	            sigInfo.level=((iw_statistics *)req.u.data.pointer)->qual.level - 256;
	        }
		// Push to data!
		sl.push_back(0);
		sl.push_back(sigInfo.level);
		data->wifiQueue.push(sl);
		sl.clear();
		cout << sigInfo.level << endl;
		sleep(3);
	}
	
} // wifi_handler



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
    data->sockHealth[id] = failureCodes::noFailure;

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
            LM->append("Successful Connect1!\n");
            data->sockHealth[id] = failureCodes::socketConnected;
        } // if
	// Give the system a 50 ms break... all work and no play makes the cpu hurt.
	sleep(1);
    } //while
    google::protobuf::io::ZeroCopyOutputStream* ZCO;
    if(stream != NULL){
        ZCO = new google::protobuf::io::FileOutputStream(stream->m_sd);
    }else{
        LM->append("Dead Stream!\n");
        delete connector;
        delete stream;
        return;
    }
    while (!data->timeToDieMap[id]) {
        switch (id) {
            case sockKeys::ACCEL:
                if (data->sendAccel) {
                    if (!data->accelQueue.isEmpty()) {
			LM->append("DEBUG3\n"); 
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

                        if (!writeDelimitedTo(GM, ZCO) || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = failureCodes::SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }

                    } // if accelQueue is not empty
                } // if sendAccel
                break;

            case sockKeys::GYRO:
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
                        if (!writeDelimitedTo(GM, ZCO) || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = failureCodes::SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if gyroQueue is not empty
                } // if sendGyro
                break;
            case sockKeys::ALTITUDE:
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
                        if (!writeDelimitedTo(GM, ZCO) || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = failureCodes::SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if altitude queue is not empty
                } // if send alt
                break;
            case sockKeys::ATTITUDE:
                if (data->sendAtt) {
                    // if there is data, send it!
                    if (!data->attitudeQueue.isEmpty()) {
                        // grab the data and remove it from the queue
                        vector<double> att = data->attitudeQueue.front();
                        data->attitudeQueue.pop();
                        // set the message type equal to the id
                        GM.set_msgtype((google::protobuf::int32) id);
                        GM.set_time((double) att[0]);
                        GM.mutable_attitude()->set_roll(att[1]);
                        GM.mutable_attitude()->set_pitch(att[2]);
                        GM.mutable_attitude()->set_yaw(att[3]);
                        // Serialize and send it!
                        if (!writeDelimitedTo(GM, ZCO) || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = failureCodes::SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if attitude queue is not empty
                } // if send att
                break;
            case sockKeys::TEMP:
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
                        if (!writeDelimitedTo(GM, ZCO) || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = failureCodes::SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if altitude queue is not empty
                } // if sendTemp
                break;
            case sockKeys::BAT:
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
                        if (!writeDelimitedTo(GM, ZCO) || stream == NULL) {
                            snprintf(cbuff, 256, "Socket %d Failure.  Exception: Dead Socket\n", id);
                            string sbuff(cbuff);
                            LM->append(sbuff);
                            data->sockHealth[id] = failureCodes::SOCKET_FAILURE;
                            data->timeToDieMap[id] = true;
                        }
                        buff.clear();
                    } // if altitude queue is not empty
                }// if sendTemp
                break;
            default:
                data->sockHealth[id] = failureCodes::UNK_SOCK;
                data->timeToDieMap[id] = true;
        } // switch
    } // while(!timeToDie)
    delete connector;
    data->sockHealth[id] = failureCodes::socketDisconnected;
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
    string local_ip = "127.0.0.1";
    acceptor = new TCPAcceptor(SERVER_PORT_NUMBER, local_ip.c_str());

    // Initialize the TCP connector
    LM->append("Initializing Server\n");
    while (!data->timeToDieMap[timeToDieFlags::SERVER_SHUTDOWN]) {
        if (acceptor->start() == 0) {
            stream = acceptor->accept();
            if (stream != NULL) {
                LM->append("Successful Accept\n");
                streams.push_back(stream);
                new thread(&IOManager::pre_acceptor_handler, this, stream);
                stream = NULL;
            } else {
                LM->append("Unsuccessful Receive.\n");
            } // if stream
        } // if start
	// Take a break...
	sleep(50);
    } // while
    delete acceptor;
} //server_handler


//----------------------------------------------------------------------------//
// pre_acceptor_handler grabs the first message from an unknown client and 
// launches the appropriate acceptor_handler
//----------------------------------------------------------------------------//

void
IOManager::pre_acceptor_handler(TCPStream* stream) {

    // Grab the generic message
    T7::GenericMessage GM;

    int id;
    char pbuff[256];

    // Create an input stream
    google::protobuf::io::ZeroCopyInputStream* ZIS = new google::protobuf::io::FileInputStream(stream->m_sd);
    if (readDelimitedFrom(ZIS, &GM)) {
        LM->append("New Acceptor Received!\n");
        id = (int) GM.msgtype();
        // ONLY RE LAUNCH IF THE CURRENT ONE IS DEAD!
        if (sockThreadMap[id] == NULL || data->sockHealth[id] == failureCodes::socketDisconnected) {
            snprintf(pbuff, 256, "Launching Acceptor for Socket %d!\n", id);
            string sbuff(pbuff);
            LM->append(sbuff);
            data->timeToDieMap[id] = false;
            data->sockHealth[id] = failureCodes::noFailure;
            sockThreadMap[id] = new thread(&IOManager::acceptor_handler, this, stream, id, GM);
        }
    }
    delete ZIS;
} // pre_acceptor_handler

//----------------------------------------------------------------------------//
// acceptor_handler deals with an acceptor and continuously reads from the stream
//----------------------------------------------------------------------------//

void
IOManager::acceptor_handler(TCPStream* stream, int id, T7::GenericMessage GM) {

    // Initialize the buffer
    char pbuff[256];

    // Initialize the socket health
    data->sockHealth[id] = failureCodes::socketConnected;
    data->timeToDieMap[id] = false;

    vector< double > vel;
    vector< double > att;
    vector< double > alt;
    vector< double > bat;

    google::protobuf::io::ZeroCopyInputStream* ZIS = new google::protobuf::io::FileInputStream(stream->m_sd);

    bool firstMessage = true;
    while (!data->timeToDieMap[id]) {
        switch (id) {
            case sockKeys::HEARTBEAT:
                if (firstMessage || readDelimitedFrom(ZIS, &GM)) {
                    firstMessage = false;
                    if ((int) GM.msgtype() == id) {
                        if (GM.heartbeat().alive()) {
                            data->HSSAlive = true;
                            LM->append("RECIEVED: HSSAlive == True\n");
                        } else {
                            data->HSSAlive = false;
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
            case sockKeys::TERMINATE:
                if (firstMessage || readDelimitedFrom(ZIS, &GM)) {
                    firstMessage = false;
                    if ((int) GM.msgtype() == id) {
                        switch (GM.terminate().terminatekey()) {
                            case terminateKeys::SELF_TERMINATE:
                                LM->append("RECIEVED: SELF TERMINATE COMMAND!\n");
                                data->timeToDieMap[id] = true;
                                data->timeToDieMap[timeToDieFlags::GLOBAL_SHUTDOWN] = true;
                                data->SystemHealth = failureCodes::RESTART;
                                break;
                            case terminateKeys::SOFT_SHUTDOWN:
                                // TODO 05May2017 att
                                LM->append("RECIEVED: SOFT SHUTDOWN COMMAND!\n");
                                break;
                            case terminateKeys::EMERGENCY_STOP:
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
                } else {
                    snprintf(pbuff, 256, "Dead Stream!  ID = %d\n", id);
                    string sbuff(pbuff);
                    LM->append(sbuff);
                    data->timeToDieMap[id] = true;
                }
                break;
            case sockKeys::CONFIG_DATA:
                if (firstMessage || readDelimitedFrom(ZIS, &GM)) {
                    firstMessage = false;
                    switch (GM.configdata().configkey()) {
                        case toggleKeys::TOGGLE_ACCEL:
                            LM->append("RECIEVED: Toggle Acceleration!\n");
                            data->sendAccel = !data->sendAccel;
                            break;
                        case toggleKeys::TOGGLE_GYRO:
                            LM->append("RECIEVED: Toggle Gyro!\n");
                            data->sendGyro = !data->sendGyro;
                            break;
                        case toggleKeys::TOGGLE_ALTITUDE:
                            LM->append("RECIEVED: Toggle Altitude!\n");
                            data->sendAlt = !data->sendAlt;
                            break;
                        case toggleKeys::TOGGLE_ATTITUDE:
                            LM->append("RECIEVED: Toggle Attitude!\n");
                            data->sendAtt = !data->sendAtt;
                            break;
                        case toggleKeys::TOGGLE_TEMP:
                            LM->append("RECIEVED: Toggle Temperature!\n");
                            data->sendTemp = !data->sendTemp;
                            break;
                        case toggleKeys::TOGGLE_BAT:
                            LM->append("RECIEVED: Toggle Bat!\n");
                            data->sendBat = !data->sendBat;
                            break;
                        default:
                            LM->append("Unidentified ConfigData Message.\n");
                            break;
                    } // switch      
                } else {
                    snprintf(pbuff, 256, "Dead Stream!  ID = %d\n", id);
                    string sbuff(pbuff);
                    LM->append(sbuff);
                    data->timeToDieMap[id] = true;
                }
                break;
            case sockKeys::MOVE_CAMERA:
                if (firstMessage || readDelimitedFrom(ZIS, &GM)) {
                    firstMessage = false;
                    switch (GM.movecamera().arrowkey()) {
                        case camKeys::MOVE_UP:
                            LM->append("RECIEVED: Move Camera Up Command!\n");
                            break;
                        case camKeys::MOVE_RIGHT:
                            LM->append("RECIEVED: Move Camera Right Command!\n");
                            break;
                        case camKeys::MOVE_DOWN:
                            LM->append("RECIEVED: Move Camera Down Command!\n");
                            break;
                        case camKeys::MOVE_LEFT:
                            LM->append("RECIEVED: Move Camera Left Command!\n");
                            break;
                        default:
                            LM->append("Unidentified Move Camera Command.\n");
                            break;
                    } // switch     
                } else {
                    snprintf(pbuff, 256, "Dead Stream!  ID = %d\n", id);
                    string sbuff(pbuff);
                    LM->append(sbuff);
                    data->timeToDieMap[id] = true;
                }
                break;
	    case sockKeys::PIXHAWK:
		if (firstMessage || readDelimitedFrom(ZIS, &GM)){
			firstMessage = false;
			// Display!// 
			printf("vel: x:%0.6f y: %0.6f z: %0.6f\n",GM.pixhawk().velx(),GM.pixhawk().vely(),GM.pixhawk().velz());
			printf("att: r:%0.6f p: %0.6f y: %0.6f\n",GM.pixhawk().roll(),GM.pixhawk().pitch(),GM.pixhawk().yaw());
			printf("alt:%0.6f\n",GM.pixhawk().altitude());
			printf("bat:%0.6f\n",GM.pixhawk().battery());

			vel.push_back(0);
			vel.push_back(GM.pixhawk().velx());
			vel.push_back(GM.pixhawk().vely());
			vel.push_back(GM.pixhawk().velz());
			
			att.push_back(0);
			att.push_back(GM.pixhawk().roll());
			att.push_back(GM.pixhawk().pitch());
			att.push_back(GM.pixhawk().yaw());
			
			alt.push_back(0);
			alt.push_back(GM.pixhawk().altitude());
			
			bat.push_back(0);
			bat.push_back(GM.pixhawk().battery());
		
			// Push the data to the queue!
			data->accelQueue.push(vel);
			data->attitudeQueue.push(att);
			data->altitudeQueue.push(alt);
			data->batteryQueue.push(bat);	
		} else {
                    snprintf(pbuff, 256, "Dead Stream!  ID = %d\n", id);
                    string sbuff(pbuff);
                    LM->append(sbuff);
                    data->timeToDieMap[id] = true;
                }
		break;
            default:
                data->sockHealth[id] = failureCodes::UNK_SOCK;
                data->timeToDieMap[id] = true;
        } //switch
    } // while

    data->sockHealth[id] = failureCodes::socketDisconnected;

    delete ZIS;
    return;
} // acceptor_handler

void
IOManager::clean() {
    // Let them die off
    data->set_threads_timeToDie(true);
    
    // close the sockets.... just to be sure!
    for(vector<TCPStream*>::iterator it = streams.begin(); it != streams.end(); ++it) {
        (*it)->close_socket();
    }    
} // clean

void
IOManager::relaunch_client(int id) {
    if (sockThreadMap[id] == NULL || data->sockHealth[id] == failureCodes::socketDisconnected) {
        char pbuff[256];
        snprintf(pbuff, 256, "Re-launching Socket:  ID = %d\n", id);
        string sbuff(pbuff);
        LM->append(sbuff);

        // reset the socket codes
        data->sockHealth[id] = failureCodes::noFailure;
        data->timeToDieMap[id] = false;
        sockThreadMap[id] = new thread(&IOManager::client_handler, this, id);
    } // if
} // relaunch_client

// -------------------------------------------------------------------------- //
// Constructors and Destructor
// -------------------------------------------------------------------------- //

IOManager::IOManager() {
    // Initialize the Logger Instance
    LM = LogManager::getInstance();

    // Initialize the Data Instance
    data = DataManager::getInstance();

}

IOManager::IOManager(const IOManager & orig) {
}

IOManager::~IOManager() {
    clean();
}

bool 
IOManager::writeDelimitedTo(T7::GenericMessage message, google::protobuf::io::ZeroCopyOutputStream * rawOutput) {
    // We create a new coded stream for each message.  Don't worry, this is fast.
    google::protobuf::io::CodedOutputStream output(rawOutput);

    // Write the size.
    const int size = message.ByteSize();
    output.WriteVarint32(size);

    uint8_t* buffer = output.GetDirectBufferForNBytesAndAdvance(size);
    if (false || buffer != NULL) {
        // Optimization:  The message fits in one buffer, so use the faster
        // direct-to-array serialization path.
        message.SerializeWithCachedSizesToArray(buffer);
    } else {
        // Slightly-slower path when the message is multiple buffers.
        message.SerializeWithCachedSizes(&output);
        if (output.HadError()) return false;
    }

    return true;
}

bool 
IOManager::readDelimitedFrom(google::protobuf::io::ZeroCopyInputStream* rawInput, T7::GenericMessage * message) {
    // We create a new coded stream for each message.  Don't worry, this is fast,
    // and it makes sure the 64MB total size limit is imposed per-message rather
    // than on the whole stream.  (See the CodedInputStream interface for more
    // info on this limit.)
    google::protobuf::io::CodedInputStream input(rawInput);

    // Read the size.
    uint32_t size;
    if (!input.ReadVarint32(&size)) return false;

    // Tell the stream not to read beyond that size.
    google::protobuf::io::CodedInputStream::Limit limit =
            input.PushLimit(size);

    // Parse the message.
    if (!message->MergeFromCodedStream(&input)) return false;
    if (!input.ConsumedEntireMessage()) return false;

    // Release the limit.
    input.PopLimit(limit);

    return true;
}





