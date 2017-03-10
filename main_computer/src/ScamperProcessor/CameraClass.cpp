/*------------------------------------------------------------------------------
Function Name: CameraClass.cpp

 * This class handles all camera related functionality
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
    10 March 2017 - t3 - Happy Birthday!
--------------------------------------------------------------------------------
 */

#include <queue>

#include "CameraClass.h"

void CameraClass::run(){
    
    // 1. Open the video stream
    VideoCapture vidSream(CAMID);
    
    // 2. Check to make sure it opened
    // TODO: 10 Mar 2017 t3 - add retries.
    if (!vidSream.isOpened()) { 
        cout << "cannot open camera";
        kill();
        return;
    }
    
    // 3. Initialize the loop parameters
    Mat cameraFrame;
    
    // 4. Loop until you get the KYS message
    while(!KYS)
    {
        // Read the camera stream and store it in the frame
        vidSream.read(cameraFrame);
        
        // If the debug flag is on, show the image!
        if(debugFlag){
            imshow("cameraFrame",cameraFrame);
            waitKey(10);
        }
        
        // Shove it in the data queue
        cameraQueue.push(cameraFrame);   
    }
}
int CameraClass::num_data(){
    return cameraQueue.size();
}

void CameraClass::clear_queue( )
{
   std::queue<Mat> empty;
   std::swap( cameraQueue, empty );
}

void CameraClass::kill(){
    std::cout<<"Camera Kill Message Received.\nByeBye.\n";
    KYS = true;
}


CameraClass::CameraClass() {
}

CameraClass::CameraClass(const CameraClass& orig) {
}

CameraClass::~CameraClass() {
}

