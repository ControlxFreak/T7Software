/*------------------------------------------------------------------------------
Function Name: main.cpp

 * This is the main function of the Scamper main on-board computer.  
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

#include <cstdlib>
#include <iostream>
#include "version.h"
#include "coreProcessor.h"


using namespace std;

//----------------------------------------------------------------------------//

void display_welcome_screen() {
    std::cout << "|-----------------------------------------------------|\n"
              << "Initializing Scamper Main On-Board Processor\n"
              << "Version : " << VERSION_NUMBER << "\n"
              << "Build Date: " << BUILD_DATE << "\n"
              << "Built By: " << BUILD_OWNER << "\n"
              << "|-----------------------------------------------------|\n"
              << "Lockheed Martin \n"
              << "Engineering Leadership Development Team \n"
              << "Team 7 \n"
              << "|-----------------------------------------------------|\n";
} // display welcome_screen()

//----------------------------------------------------------------------------//

int main(int argc, char** argv) {

    // Display the Welcome Screen
    // TBD: <t3, 20170215> add logging of standard output to a log file
    display_welcome_screen();

    // Launch the pre processor.  
    coreProcessor CP = new coreProcessor();
    try{
        int rc = CP.pre_launch();
        if (rc) throw -1;
    } catch (...){
        ~CP;
        return -1;
    }
    
    // Launch the Core Processor!
    try{
        int rc = CP.launch();
        if (rc) throw -1;
    } catch (...){
        ~CP;
        return -1;
    }
    
    // All done!
    return 0;
} // main