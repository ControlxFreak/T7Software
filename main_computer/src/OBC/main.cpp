/*------------------------------------------------------------------------------
Function Name: main.cpp

 * This is the main function of the UAV main on-board computer.  
 * This function displays the welcome screen and launches the core processor

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
    22 Apr 2017 - t3 - Added relaunch logic
    21 May 2017 - t3 - Added quit handler
--------------------------------------------------------------------------------
 */

#include "Executive.h"
#include "LogManager.h"
#include "T7Types.h"
#include <signal.h>

using namespace std;

// ------------------------------------------------------------------------------
//   Quit Signal Handler
// ------------------------------------------------------------------------------
// this function is called when you press Ctrl-C

void
quit_handler(int s) {
    LogManager* LM = LogManager::getInstance();
    LM->append("\n");
    LM->append("TERMINATING AT USER REQUEST\n");
    LM->append("\n");

    Executive* Exec = Executive::getInstance();
    Exec->clean();

    // end program here
    exit(0);
} // quit_handler

// ------------------------------------------------------------------------------
//   Quit Signal Handler
// ------------------------------------------------------------------------------
// this function is called when you press Ctrl-C

int
main() {
    // Initialize the Executive
    Executive* Exec = Executive::getInstance();

    // Setup the quit handler
    struct sigaction sigIntHandler;

    sigIntHandler.sa_handler = quit_handler;
    sigemptyset(&sigIntHandler.sa_mask);
    sigIntHandler.sa_flags = 0;

    sigaction(SIGINT, &sigIntHandler, NULL);

    while (true) {
        try {
            // Launch the Executive
            Exec->launch();
            // If the watchdog says that we are complete, then do it!
            if (Exec->data->SystemHealth != failureCodes::RESTART){ break; }

            Exec->needsCleaning = true;
        } catch (...) {} // try catch
    } // while (true)

    // Cleanup!!
    Exec->clean();
    // Return the code determined by the executive
    return 0;
} // main


