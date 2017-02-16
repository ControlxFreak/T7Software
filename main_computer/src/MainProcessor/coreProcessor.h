/*------------------------------------------------------------------------------
Function Name: coreProcessor.h

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
#include "MissionParameters.h"
#include "IOProcessor.h"

class coreProcessor {
public:
    // Properties
    MissionParameters MP;
    IOProcessor IO;    
        
    // Methods
    coreProcessor();
    coreProcessor(const coreProcessor& orig);
    virtual ~coreProcessor();
    int pre_launch();    
private:

};

#endif /* COREPROCESSOR_H */

