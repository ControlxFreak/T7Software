/*------------------------------------------------------------------------------
Function Name: Executive.cpp

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
    08 April 2017 - t3 - Happy Birthday!
--------------------------------------------------------------------------------
 */

#include <string>
#include "Executive.h"
#include "WatchDog.h"
#include "T7Types.h"

using namespace std;
//----------------------------------------------------------------------------//
// launch() method launches the executive and all of it's managers 
//----------------------------------------------------------------------------//
void 
Executive::launch()
{
    // Cleanup so we can start fresh!
    if(needsCleaning){ clean(); }
    
    // Print out the header
    LM->appendHeader();

    // Launch the thread manager
    LM->append("Launching IO Manager\n");
    IO->launch();
  
    // Run the executive
    run();
} //launch()

//----------------------------------------------------------------------------//
// clean() method cleans the executive and all of it's managers up
//----------------------------------------------------------------------------//
void 
Executive::clean()
{ 
    LM->append("Executive Cleaning Up!\n");
    
    // Tell Everyone To Die... incase it hasn't happened already
    data->set_all_timeToDie(true);
            
    // Clean up the managers!
    LM->append("Cleaning IO Manager!\n");
    IO->clean();
    
    LM->append("Cleaning Data!\n");
    data->clean();
        
    LM->append("Cleaning Logger!\n");
    LM->clean();
} // clean()

//----------------------------------------------------------------------------//
// run() method is in charge of handling all of the executive decisions that 
//       must be made based on data from the managers
//----------------------------------------------------------------------------//
void 
Executive::run()
{
    LM->append("Running Executive\n");
    WatchDog WD = WatchDog();
    
    vector< double > gyro;
    vector< double > accel;
    // Loop until it is time to die
    while(!data->timeToDieMap[timeToDieFlags::EXECUTIVE_SHUTDOWN])
    {
        if(data->accelQueue.size() < 10)
        {
            accel.push_back(0);
            accel.push_back(100);
            accel.push_back(3);
            accel.push_back(-9.3);

            gyro.push_back(999);
            gyro.push_back(99999);
            gyro.push_back(12345);
            gyro.push_back(000);
            
            data->accelQueue.push(accel);
            data->gyroQueue.push(gyro);
            
            accel.clear();
            gyro.clear();
        }
        WD.check();
    } //while(!timeToDie)
} //run()

//----------------------------------------------------------------------------//
// Constructors 
//----------------------------------------------------------------------------//
Executive::Executive() {
    // Initialize the Logger Instance
    LM = LogManager::getInstance();
    
    // Initialize the Data Instance
    data = DataManager::getInstance();
    
    // Initialize the IOManager
    IO = IOManager::getInstance();

}
Executive::Executive(const Executive& orig) {}
//----------------------------------------------------------------------------//
// Destructor - Although the singletons are not explicitly deleted but yet, 
//              Implicitly deleted at the end of the program, at least clean up 
//              the memory.
//----------------------------------------------------------------------------//
Executive::~Executive() {clean();}

