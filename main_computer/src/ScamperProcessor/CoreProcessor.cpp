/*------------------------------------------------------------------------------
Function Name: CoreProcessor.cpp

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

int CoreProcessor::pre_launch() {

    // 1. Read and store the Mission Parameters
    int rc = MP.read();
    if (rc) {
        std::cout << "Failed to load the Mission Parameters...\n"
                << "exiting gracefully\n";
        return rc;
    } // if rc    

    
    // 2. Set the TCP Parameters
    std::cout << "Setting TCP Parameters!\n";
    TCP.set_params(&MP);

    
    // 3. Initialize the connection
    std::cout << "Initializing TCP connection!\n";
    TCP.init_connection();

    return 0;
}
//----------------------------------------------------------------------------//
// Infinite loop 

void CoreProcessor::launch() {

    // 1. Initialize the loop variables
    std::cout <<"Initializing the loop variables!\n";
    std::cout << "Launching the TCP Receive Thread!\n";
    
    // 2. Launch the receive thread
    std::thread tcp_rec_thread(&TCPClass::receive_loop, std::ref(TCP));
    int MID;
    std::string data;
    std::vector<std::string> data_array;
    
    // 3. Loop until we receive the KYS message
    while (!TCP.KYS) {
        
        // If we received data, do something with it!
        if (TCP.num_data() > 0) {
            
            // Grab the MID and the data
            data_array = TCP.get_data();
            MID = stoi(data_array.front());
            
            data = data_array.back();

            // Switch with the data based on the MID
            switch(MID){
                case 666 : TCP.kill();
                default: std::cout<<"Unknown Message ID!\nDiscarding Data.";
            } // MID
        } // if num_data
    } // while !TCP.KYS

    
    tcp_rec_thread.join();
    //tcp_send_thread.join();
}

//----------------------------------------------------------------------------//
// Constructors and Destructors

CoreProcessor::CoreProcessor() {
}

CoreProcessor::CoreProcessor(const CoreProcessor& orig) {
}

CoreProcessor::~CoreProcessor() {
}

