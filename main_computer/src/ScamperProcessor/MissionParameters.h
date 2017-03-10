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

#include "pugixml.hpp"

struct comStruct{
    uint16_t tcp_rec_port;
    uint16_t tcp_sen_port;
    uint16_t serial_port;
    uint16_t serial_baudrate;
};

struct senStruct{
    bool altitude;
    bool optical;
};

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
    
    void setComParams(comStruct);
    void setSensorParams(senStruct);
    void readComParams(pugi::xml_node);
    void readSensorParams(pugi::xml_node);
    bool to_bool(std::string str);
private:

};

#endif /* MISSIONPARAMETERS_H */

