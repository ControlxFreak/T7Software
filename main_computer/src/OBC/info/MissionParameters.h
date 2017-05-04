/*------------------------------------------------------------------------------
Function Name: MissionParameters.h
 * This class is responsible for storing and executing all mission parameter 
 * related functionality.
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
    13 Mar 2017 - t3 - Update for PortPerMessage logic.  Now each MID gets its 
                       own port
--------------------------------------------------------------------------------
 */

#ifndef MISSIONPARAMETERS_H
#define MISSIONPARAMETERS_H

#include <iostream>
#include <string>
#include <stdlib.h> 
#include <sstream>
#include <iomanip>
#include <algorithm>
#include <cctype>

#include "../external/pugi/pugixml.hpp"
#include "LogManager.h"

using namespace std;

struct msgInfo {
    string name;
    int MID;
    bool IHost;
    string DataType;
};


struct comStruct {

    struct {
        const char* hsIP;
        uint16_t port;
        int nMID;

        vector<msgInfo> msgTable;
    } tcp;

    struct {
        uint16_t Port;
        uint16_t BaudRate;
    } serial;

};

struct senStruct{
    bool altitude;
    bool optical;
};


using namespace std;
class MissionParameters {
public:
    // Properties
    comStruct ComParams;
    senStruct SensorParams;
    
    // Methods
    MissionParameters();
    MissionParameters(const MissionParameters& orig);
    virtual ~MissionParameters();
    void read();
    void fail();
    void clear(){}
    
    void setComParams(comStruct);
    void setSensorParams(senStruct);
    void readComParams(pugi::xml_node);
    void readSensorParams(pugi::xml_node);
    bool to_bool(std::string str);
private:

};

#endif /* MISSIONPARAMETERS_H */

