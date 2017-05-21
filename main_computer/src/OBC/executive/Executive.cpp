/*------------------------------------------------------------------------------
Function Name: Executive.h

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


#include "Executive.h"

//----------------------------------------------------------------------------//
// launch() method launches the executive and all of it's managers 
//----------------------------------------------------------------------------//
void Executive::launch()
{
    // Cleanup so we can start fresh!
    if(needsCleaning) clean();
    
    // Initialize the Logger Instance
    LM = LogManager::getInstance();
       
    // Print out the header
    LM->appendHeader();
    
    // Initialize the Data Singleton
    LM->append("Initializing the Data Singleton\n");
    data = DataManager::getInstance();
   
    // Initialize the Watchdog
    LM->append("Initializing the WatchDog Singleton\n");
    WD = WatchDog::getInstance();
    
    // Launch the thread manager
    LM->append("Launching Thread Manager\n");
    TM.launch(&IO);
  
    // Run the executive
    run();
} //launch()

//----------------------------------------------------------------------------//
// clean() method cleans the executive and all of it's managers up
//----------------------------------------------------------------------------//
void Executive::clean()
{
    LM->append("Cleaning Up!\n");
    // Clean yourself up!
    timeToDie = false;
    
    // Clean up the managers!
    LM->append("Cleaning Data!\n");
    data->clean();
    
    LM->append("Cleaning Thread Manager!\n");
    TM.clean();
    
    LM->append("Cleaning Watch Dog!\n");
    WD->clean();
    
    LM->append("Cleaning IO Manager!\n");
    IO.clean();    
    
    LM->append("Cleaning Logger!\n");
    LM->clean();
} // clean()

//----------------------------------------------------------------------------//
// run() method is in charge of handling all of the executive decisions that 
//       must be made based on data from the managers
//----------------------------------------------------------------------------//
void Executive::run()
{
    LM->append("Running Executive\n");

    vector< double > accel;
    // Loop until it is time to die
    while(!timeToDie)
    {
        if(data->accelQueue.size() < 10)
        {
            accel.push_back(0);
            accel.push_back(100);
            accel.push_back(3);
            accel.push_back(-9.3);

            data->accelQueue.push(accel);
            accel.clear();
        }
        usleep(3e6);
        // TODO: Add WatchDog stuff here.
        if(data->globalShutdown) {timeToDie = true;break;}
    } //while(!timeToDie)
} //run()

//----------------------------------------------------------------------------//
// Constructors 
//----------------------------------------------------------------------------//
Executive::Executive() {}
Executive::Executive(const Executive& orig) {}
//----------------------------------------------------------------------------//
// Destructor - Although the singletons are not explicitly deleted but yet, 
//              Implicitly deleted at the end of the program, at least clean up 
//              the memory.
//----------------------------------------------------------------------------//
Executive::~Executive() {clean();}

