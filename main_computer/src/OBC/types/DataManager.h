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
    21 May 2017 - t3 - Added individualized timeToDieFlags
--------------------------------------------------------------------------------
 */

#ifndef DATA_H
#define DATA_H
#include "TSQueue.h"
#include <map>

using namespace std;

class DataManager {
public:

    // Data Queues
    TSQueue< vector<double> > accelQueue;
    TSQueue< vector<double> > gyroQueue;
    TSQueue< vector<double> > attitudeQueue;
    TSQueue< vector<double> > altitudeQueue;
    TSQueue< vector<double> > batteryQueue;
    TSQueue< vector<double> > tempQueue;

    // Toggle Send Flags
    bool sendAccel = true;
    bool sendGyro = true;
    bool sendAlt = true;
    bool sendAtt = true;
    bool sendTemp = true;
    bool sendBat = true;

 
    // socket health
    map<int, int>sockHealth;

    // thread Death
    map<int,bool>timeToDieMap;

    // Define the socket keys as exactly the same values that the protobuf enum msgType has.
    // TODO: att - potentially just import that enumeration.

    enum
    sockKeys {
        // INFO MESSAGE //
        RESPONSE = 0,
        HEARTBEAT = 1,
        TERMINATE = 2,

        // HSS -> UAV //
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
    
   // TimeToDieFlags
    enum
    timeToDieFlags {
        GLOBAL_SHUTDOWN = -99,
        EXECUTIVE_SHUTDOWN = -98,
        WATCHDOG_SHUTDOWN = -97,
        SERVER_SHUTDOWN = 999,
        HEARTBEAT_SHUTDOWN = sockKeys::HEARTBEAT,
        TERMINATE_SHUTDOWN = sockKeys::TERMINATE,
        CONFIG_SHUTDOWN = sockKeys::CONFIG_DATA,
        MOVE_SHUTDOWN = sockKeys::MOVE_CAMERA,
        ACCEL_SHUTDOWN = sockKeys::ACCEL,
        GYRO_SHUTDOWN = sockKeys::GYRO,
        ALT_SHUTDOWN = sockKeys::ALTITUDE,
        ATT_SHUTDOWN = sockKeys::ATTITUDE,
        TEMP_SHUTDOWN = sockKeys::TEMP,
        BAT_SHUTDOWN = sockKeys::BAT
    };
    
    // Define the terminate keys
    enum
    terminateKeys {
        SELF_TERMINATE = 0,
        SOFT_SHUTDOWN = 1,
        EMERGENCY_STOP = 2
    };

    // Define the toggle keys
    enum 
    toggleKeys {
        TOGGLE_ACCEL = 0,
        TOGGLE_GYRO = 1,
        TOGGLE_ALTITUDE = 2,
        TOGGLE_ATTITUDE = 3,
        TOGGLE_TEMP = 4,
        TOGGLE_BAT = 5,
    };

    // Cleanup method

    void
    clean() {
        DataManager* data = getInstance();
        data->accelQueue.clear();
        data->gyroQueue.clear();
        data->attitudeQueue.clear();
        data->altitudeQueue.clear();
        data->batteryQueue.clear();
        data->tempQueue.clear();
    } //clean

    void
    set_threads_timeToDie(bool flag) {
        // Grab the correct instance
        DataManager* data = DataManager::getInstance();

        for (auto& m : data->timeToDieMap) {
            if (m.first < 0 ) {
                continue;
            } // don't overwrite the global shutdown!
            m.second = flag;
        } // for...    
    } //set_all_timeToDie

    void
    set_all_timeToDie(bool flag) {
        // Grab the correct instance
        DataManager* data = DataManager::getInstance();
        for (auto& m : data->timeToDieMap) {
            m.second = flag;
        } // for...    
    } //set_all_timeToDie

    // Static singleton initializer 

    static DataManager* getInstance() {
        static DataManager* p_DataManager = new DataManager();
        return p_DataManager;
    };

    DataManager() {

        timeToDieMap[GLOBAL_SHUTDOWN] = false;
        timeToDieMap[EXECUTIVE_SHUTDOWN] = false;
        timeToDieMap[WATCHDOG_SHUTDOWN] = false;
        timeToDieMap[SERVER_SHUTDOWN] = false;
        timeToDieMap[HEARTBEAT_SHUTDOWN] = false;
        timeToDieMap[TERMINATE_SHUTDOWN] = false;
        timeToDieMap[CONFIG_SHUTDOWN] = false;
        timeToDieMap[MOVE_SHUTDOWN] = false;
        timeToDieMap[ACCEL_SHUTDOWN] = false;
        timeToDieMap[GYRO_SHUTDOWN] = false;
        timeToDieMap[ALT_SHUTDOWN] = false;
        timeToDieMap[ATT_SHUTDOWN] = false;
        timeToDieMap[TEMP_SHUTDOWN] = false;
        timeToDieMap[BAT_SHUTDOWN] = false;
    };

    DataManager(const DataManager& orig) {
    };

    virtual ~DataManager() {
    };
};

#endif /* DATA_H */

