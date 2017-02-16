/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   MissionParameters.h
 * Author: controlxfreak
 *
 * Created on February 15, 2017, 7:15 PM
 */

#ifndef MISSIONPARAMETERS_H
#define MISSIONPARAMETERS_H

class MissionParameters {
public:
    // Properties
    // TCP Communication:
    char home_station_ip;
    int tcp_port;
    // Serial Communication:
    char serial_port;
    int  serial_baudrate;
    
    // Methods
    MissionParameters();
    MissionParameters(const MissionParameters& orig);
    virtual ~MissionParameters();
    int read();
private:

};

#endif /* MISSIONPARAMETERS_H */

