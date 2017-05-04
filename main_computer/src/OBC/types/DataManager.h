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

#ifndef DATA_H
#define DATA_H
#include <string>
#include <queue>
#include <map>
#include <vector>
#include "boost/thread.hpp"
#include "TSQueue.h"

using namespace std;

class DataManager {
        
public:
    
    // Vector of Message ID's
    vector<int> midVec;
    
    // Data Queues
    TSQueue<int>                 masterQueue;
    TSQueue< vector<double> >     accelQueue;
    TSQueue< vector<double> >     gyroQueue;
    TSQueue< vector<double> >     attitudeQueue;
    TSQueue< vector<double> >     altitudeQueue;
    TSQueue< vector<double> >     batteryQueue;
    TSQueue< vector<double> >     tempQueue;
    
    // Toggle Send Flags
    bool sendAccel = true;
    bool sendGyro = true;
    bool sendAlt = true;
    bool sendAtt = true;
    bool sendTemp = true;
    bool sendBat = true;
    
    bool HSSAlive = false;
    bool globalShutdown = false;
    
    // Define the socket keys as exactly the same values that the protobuf enum msgType has.
    // TODO: att - potentially just import that enumeration.
    enum sockKeys {
            // INFO MESSAGE //
            RESPONSE = 0,
            HEARTBEAT = 1,
            TERMINATE = 2,

            // HSS -> UAV //
            UPDATE_PARAM = 100,
            CONFIG_DATA = 101,
            MOVE_CAMERA = 102,

            // UAV -> HSS //
            ACCEL = 200,
            GYRO = 201,
            ALTITUDE = 202,
            ATTITUDE = 203,
            TEMP = 204,
            BAT = 205
    };
    
    // Health Meters
    
    // Cleanup method
    void clean()
    {
        DataManager* data = getInstance();
        data->midVec.clear();
        data->masterQueue.clear();
        data->accelQueue.clear();
        data->gyroQueue.clear();
        data->attitudeQueue.clear();
        data->altitudeQueue.clear();
        data->batteryQueue.clear();
        data->tempQueue.clear();
    }
    
    // Static singleton initializer 
    static DataManager* getInstance()
    {
        static DataManager* p_DataManager = new DataManager();
        return p_DataManager;
    };
    
    DataManager(){};
    DataManager(const DataManager& orig){};
    virtual ~DataManager(){};
};

#endif /* DATA_H */

