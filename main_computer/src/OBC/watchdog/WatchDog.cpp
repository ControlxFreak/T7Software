/*------------------------------------------------------------------------------
Function Name: WatchDog.cpp

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
// check() this method that will check the health of the OBC software
//----------------------------------------------------------------------------//

void
WatchDog::check() {

    // ---------------------------------------------------------------------- //
    //Check the time to die map!
    if (data->timeToDieMap[timeToDieFlags::GLOBAL_SHUTDOWN]) {
        data->set_all_timeToDie(true);
        return;
    } //global shutdown

    // ---------------------------------------------------------------------- //
    // Check the socket healths
    if (data->sockHealth[sockKeys::ACCEL] == failureCodes::socketDisconnected) {
        IO->relaunch_client(sockKeys::ACCEL);
    }
    if (data->sockHealth[sockKeys::ALTITUDE] == failureCodes::socketDisconnected) {
        IO->relaunch_client(sockKeys::ALTITUDE);
    }
    if (data->sockHealth[sockKeys::ATTITUDE] == failureCodes::socketDisconnected) {
        IO->relaunch_client(sockKeys::ATTITUDE);
    }
    if (data->sockHealth[sockKeys::BAT] == failureCodes::socketDisconnected) {
        IO->relaunch_client(sockKeys::BAT);
    }
    if (data->sockHealth[sockKeys::GYRO] == failureCodes::socketDisconnected) {
        IO->relaunch_client(sockKeys::GYRO);
    }
    if (data->sockHealth[sockKeys::TEMP] == failureCodes::socketDisconnected) {
        IO->relaunch_client(sockKeys::TEMP);
    }

} // launch()


//----------------------------------------------------------------------------//
// Constructors and Destructors
//----------------------------------------------------------------------------//

WatchDog::WatchDog() {
    data = DataManager::getInstance();
    IO = IOManager::getInstance();
}

WatchDog::WatchDog(const WatchDog& orig) {
}

WatchDog::~WatchDog() {
}

