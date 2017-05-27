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

#ifndef EXECUTIVE_H
#define EXECUTIVE_H

#include "IOManager.h"
#include "WatchDog.h"
#include "DataManager.h"
#include "LogManager.h"

class Executive {
public:
    // ---------------------------------------------------------------------- //
    // Public Properties:
    // ---------------------------------------------------------------------- //   
    bool needsCleaning = false;
    IOManager IO;
    LogManager* LM;
    DataManager* data;
    WatchDog* WD;
    
    // ---------------------------------------------------------------------- //
    // Public Methods:
    // ---------------------------------------------------------------------- //
    static Executive* getInstance()
    {
        static Executive* p_Executive = new Executive();
        return p_Executive;
    };    
    void launch();
    void clean();
    Executive();
    Executive(const Executive& orig);
    virtual ~Executive();
private:
    // ---------------------------------------------------------------------- //
    // Private Methods:
    // ---------------------------------------------------------------------- //
    void run();
};

#endif /* EXECUTIVE_H */

