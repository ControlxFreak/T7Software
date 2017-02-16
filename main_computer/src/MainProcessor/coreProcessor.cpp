/*------------------------------------------------------------------------------
Function Name: coreProcessor.cpp

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

#include "coreProcessor.h"
#include "IOProcessor.h"

// ---------------------------------------------------------------------------//
// coreProcessor::pre_launch() is intended to setup the core processor prior to 
// establishing IO connection and beginning the infinite loop
//
// Tasks:
//      1. Read and store the mission parameters as a class property
//      2. Setup the IO Processor using the mission parameters
//      3. Establish TCP connection with the home station software
//      4. Establish Serial connection with the micro processor

int coreProcessor::pre_launch() {
    
    // 1. Read and store the Mission Parameters
    int rc = MP.read();
    if (rc) {
        std::cout << "Failed to load the Mission Parameters...\n"
                  << "exiting gracefully\n";
        return rc;
    }
    // 2. Setup the IO processor using the mission parameters
    IO = new IOProcessor(MP.home_station_ip,MP.tcp_port,MP.serial_port,MP.serial_baudrate);
    
    // 3. Establish TCP Connection
    // TBD: <t3, 20170215> add more specific error messages based on the return code
    rc = IO.establish_tcp_connection();
    if (rc) {
        std::cout << "Failed to establish a TCP connection...\n"
                  << "exiting gracefully\n";
        return rc;
    }
    
    // 4. Establish serial connection
    rc = IO.establish_serial_connection();
    if (rc) {
        std::cout << "Failed to establish a serial connection...\n"
                  << "exiting gracefully\n";
        return rc;
    }
    
    return 0;
} // pre_launch()

// ---------------------------------------------------------------------------//
// Constructor / Destructor stuff
coreProcessor::coreProcessor() {
} 

coreProcessor::coreProcessor(const coreProcessor& orig) {
}

coreProcessor::~coreProcessor() {
    ~IO;
    ~MP;
}
// ---------------------------------------------------------------------------//