/*------------------------------------------------------------------------------
Function Name: CameraClass.h

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

#ifndef CAMERACLASS_H
#define CAMERACLASS_H

#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>

#define CAMID 0

using namespace cv;
using namespace std;

class CameraClass {
public:
    
    bool KYS = false;
    bool debugFlag = true;
    std::queue<Mat> cameraQueue;
    
    void clear_queue();
    void run();
    void kill();
    int num_data();
    CameraClass();
    CameraClass(const CameraClass& orig);
    virtual ~CameraClass();
private:

};

#endif /* CAMERACLASS_H */

