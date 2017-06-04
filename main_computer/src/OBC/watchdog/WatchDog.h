/*------------------------------------------------------------------------------
Function Name: WatchDog.h

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
#include "IOManager.h"
#include "T7Types.h"

class WatchDog {
public:
    // Properties //
    DataManager* data;
    IOManager* IO;
    
    // Methods //
    void check();
    
    WatchDog();
    WatchDog(const WatchDog& orig);
    virtual ~WatchDog();
private:

};

#endif /* WATCHDOG_H */

