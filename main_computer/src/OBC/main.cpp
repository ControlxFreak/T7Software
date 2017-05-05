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
--------------------------------------------------------------------------------
 */

#include "Executive.h"
using namespace std;
int main()
{
    
    // Initialize the Executive
    Executive Exec;
     
    while(true)
    {   
        try
        {
            // Launch the Executive
            Exec.launch();
            // If the watchdog says that we are complete, then do it!
            if(Exec.WD->SystemHealth == Exec.WD->noFailure) break;
            
            Exec.needsCleaning = true;
        }catch(...){}
    }
    // Return the code determined by the executive
    return Exec.WD->SystemHealth;
}