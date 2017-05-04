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

#include "WatchDog.h"

//----------------------------------------------------------------------------//
// launch() this method is the main method that will run until the timeToDie flag 
//       is set to true.
//----------------------------------------------------------------------------//
void WatchDog::launch(){
    while(!timeToDie)
    {
        // Check for thread interruptions
        boost::this_thread::interruption_point();
    } //while(!ttd)
} // launch()

//----------------------------------------------------------------------------//
// clean() this method cleans up the watchdog
//----------------------------------------------------------------------------//
void WatchDog::clean()
{
    SystemHealth = failureCodes::noFailure; 
    timeToDie = false;
} //clean()


//----------------------------------------------------------------------------//
// kill() as the name implys, this sets the timeToDie flag to true and kills 
//        the run 
//----------------------------------------------------------------------------//
void WatchDog::kill()
{
    timeToDie = true;
} // kill()

//----------------------------------------------------------------------------//
// Constructors and Destructors
//----------------------------------------------------------------------------//
WatchDog::WatchDog() {
}

WatchDog::WatchDog(const WatchDog& orig) {
}

WatchDog::~WatchDog() {
}

