/*------------------------------------------------------------------------------
Function Name: DataManager.h

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
#include "T7Types.h"
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
    
    int SystemHealth = failureCodes::noFailure;
    bool timeToDie = false;
    bool HSSAlive = true;

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

