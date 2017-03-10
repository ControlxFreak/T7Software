/*------------------------------------------------------------------------------
Function Name: CoreProcessor.cpp
 * This is the core class that handles all of the processing for the scamper 
 * system
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

#include "CoreProcessor.h"

// ---------------------------------------------------------------------------//
// coreProcessor::pre_launch() is intended to setup the core processor prior to 
// establishing IO connection and beginning the infinite loop
//
// Tasks:
//      1. Read and store the mission parameters as a class property
//      2. Set the TCP Parameters
//      3. Initialize the connection

void CoreProcessor::pre_launch() {

    // 1. Read and store the Mission Parameters
    std::cout <<"Reading Mission Parameters\n";
    MP.read();
        
    // 2. Set the TCP Parameters
    std::cout << "Setting TCP Parameters!\n";
    TCP.set_params(&MP);

    
    // 3. Initialize the connection
    std::cout << "Initializing TCP connection!\n";
    TCP.init_connection();

}

//----------------------------------------------------------------------------//
// Infinite loop 
void CoreProcessor::launch() {

    // 1. Initialize the Loop Variables
    int MID;
    std::string data;
    std::vector<std::string> data_array;
    int prev_cam_num;
    
    // 2. Launch the threads
    // Launch the TCP thread
    std::thread tcp_rec_thread(&TCPClass::run_server, std::ref(TCP));
    std::thread tcp_send_thread;
    
    // Launch the Camera Thread
    std::thread camera_thread(&CameraClass::run, std::ref(CAM));
    
    
    // 3. Loop until we receive the KYS message
    while (!KYS) {
        
        // 3.1 Check for messages that must be sent

        
        // 3.2 If we received data, do something with it!
        if (TCP.num_data() > 0) {
            // Grab the MID and the data
            data_array = TCP.get_data();
            MID = stoi(data_array.front());
            
            data = data_array.back();

            // Switch with the data based on the MID
            switch(MID){
                case 666 : kill();
                default: std::cout<<"Unknown Message ID!\nDiscarding Data.";
            } // MID      
        } // if num_data
        // 3.3 Check the Camera Queue and send it out!
        if (CAM.num_data() > 0 && CAM.num_data() != prev_cam_num){
            // SEND!
            std::cout<<"Number of Camera Frames: "<<CAM.num_data()<<std::endl;
            
            if(CAM.num_data()>100){
                CAM.clear_queue();
            }
        }
        
        // 3.4 Check to see if the TCP received the KYS message before us 
        if (TCP.KYS){
            kill();
        }
    } // while !TCP.KYS

    
    // Join the threads and wait for them to die before leaving!
    if (tcp_rec_thread.joinable()){
        tcp_rec_thread.join();
    }
    
    if (camera_thread.joinable()){
        camera_thread.join();
    }
    
    if (tcp_send_thread.joinable()){
        tcp_send_thread.join();
    }
    
}

void CoreProcessor::kill(){
    
    std::cout<<"CoreProcessor Kill Message Received.\nByeBye.\n";
    KYS = true;
    TCP.kill();
    CAM.kill();
}


//----------------------------------------------------------------------------//
// Constructors and Destructors

CoreProcessor::CoreProcessor() {
}

CoreProcessor::CoreProcessor(const CoreProcessor& orig) {
}

CoreProcessor::~CoreProcessor() {
}

