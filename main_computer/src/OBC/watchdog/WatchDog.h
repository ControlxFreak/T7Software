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

#ifndef WATCHDOG_H
#define WATCHDOG_H
#include "DataManager.h"

class WatchDog {
public:
    
    // Properties //
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    // Failure Codes:
    //              criticalFailure: the system MUST be exited immediately.  Do not even try a restart.
    //              serriousFailure: the system MUST be restarted immediately.
    //              moderateFailure: the system SHOULD be restarted, but it probably could clean itself up first.
    //             issolatedFailure: the system doesn't need a restart but something is failed.  Initiate isolated trouble shooting.  If that doesn't work, likely will go to moderate
    //                 smallFailure: the section of code probably hiccuped. Use default settings or backup values.  Similar to isolated, but less sever.
    //                    noFailure: Good to go!
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    enum failureCodes {
                       // Generic Failures
                       critFail=-99,
                       seriousFail,
                       moderateFailure,
                       issolatedFailure,
                       smallFailure,
                       RESTART,
                       // Socket Failures
                       SOCKET_FAILURE,
                       CONNECT_FAIL,
                       UNK_SOCK, 
                       socketDisconnected,
                       //Successes
                       noFailure=0,
                       //Socket Success
                       socketConnected}; 
    
    int SystemHealth = failureCodes::noFailure;
    bool timeToDie = false;
    bool HSSAlive = true;
    
    // Methods //
    void launch();
    void kill();
    void clean();
    
    static WatchDog* getInstance()
    {
        static WatchDog* p_WatchDog = new WatchDog();
        return p_WatchDog;
    };    
    
    WatchDog();
    WatchDog(const WatchDog& orig);
    virtual ~WatchDog();
private:

};

#endif /* WATCHDOG_H */

