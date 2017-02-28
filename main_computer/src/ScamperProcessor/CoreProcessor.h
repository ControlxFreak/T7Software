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

#ifndef COREPROCESSOR_H
#define COREPROCESSOR_H


#include <iostream>
#include <string.h>
#include <cstring>
#include <thread>
#include <vector>
#include "MissionParameters.h"
#include "TCPClass.h"

class CoreProcessor {
public:
    
    //------------------------------------------------------------------------//
    //Properties
    MissionParameters MP;
    TCPClass TCP;
    
    
    //------------------------------------------------------------------------//
    //Methods
    int pre_launch();
    void launch();
    
    //------------------------------------------------------------------------//
    // Constructors and destructors
    CoreProcessor();
    CoreProcessor(const CoreProcessor& orig);
    virtual ~CoreProcessor();
private:
    std::thread tcp_thread;    
};

#endif /* COREPROCESSOR_H */

