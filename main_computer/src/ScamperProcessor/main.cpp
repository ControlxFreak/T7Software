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
#include "CoreProcessor.h"

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
int main() {

    // 1. Display the Welcome Screen!
    display_welcome_screen();
     
    // 2.Launch the pre processor.  
    std::cout<<"Beginning Prelaunch initialization!\n";
    CoreProcessor CP;
    CP.pre_launch();

    // 3. Launch the processor.
    std::cout<<"Launching the core processor!\n";
    CP.launch();
    
    std::cout<<"|-----------------------------------------------------|\n";
    std::cout<<"Successful Mission!\n";
    return 0;
}

