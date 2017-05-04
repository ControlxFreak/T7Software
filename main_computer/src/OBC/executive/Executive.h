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

#include <string>
#include "ThreadManager.h"
#include "WatchDog.h"
#include "IOManager.h"
#include "Algorithms.h"
#include "DataManager.h"
#include "LogManager.h"

using namespace std;
class Executive {
public:
    // ---------------------------------------------------------------------- //
    // Properties:
    // ---------------------------------------------------------------------- //   
    // Managers:
    ThreadManager TM;
    WatchDog WD;
    IOManager IO;
    Algorithms ALG;
    DataManager* data;
    LogManager* LM;
    
    bool needsCleaning = false;
    
    // ---------------------------------------------------------------------- //
    // Public Methods:
    // ---------------------------------------------------------------------- //
    void launch();
    void clean();
    Executive();
    Executive(const Executive& orig);
    virtual ~Executive();
private:
    
    // Properties
    bool timeToDie = false;
    // ---------------------------------------------------------------------- //
    // Private Methods:
    // ---------------------------------------------------------------------- //
    void run();
    
};

#endif /* EXECUTIVE_H */

