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
#include <string.h>
#include <cstring>
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
    TCP.set_params(MP.tcp_port);

    // 3. Initialize the connection
    TCP.init_connection();

    return 0;
}
//----------------------------------------------------------------------------//
// Infinite loop 

void CoreProcessor::launch() {

    std::thread tcp_rec_thread(&TCPClass::receive_loop, std::ref(TCP));
    std::thread tcp_send_thread(&TCPClass::send_loop, std::ref(TCP));


    while (!TCP.KYS) {
        if (TCP.num_data() > 0) {
            char* data = TCP.get_data();
            if (strcmp((const char *) data, "KYS") == 0) {
                TCP.stop();
            }
        }
    }
    
    tcp_rec_thread.join();
    tcp_send_thread.join();
}

//----------------------------------------------------------------------------//
// Constructors and Destructors

CoreProcessor::CoreProcessor() {
}

CoreProcessor::CoreProcessor(const CoreProcessor& orig) {
}

CoreProcessor::~CoreProcessor() {
}

