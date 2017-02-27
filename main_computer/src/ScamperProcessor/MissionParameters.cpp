/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   MissionParameters.cpp
 * Author: controlxfreak
 * 
 * Created on February 15, 2017, 7:15 PM
 */

#include <iostream>
#include "pugixml.hpp"
#include "MissionParameters.h"

// Read the Mission Parameters and Store them as parameters
int MissionParameters::read(){
    
    // Load and read the file
    pugi::xml_document mission_params_file;
    
    if(!mission_params_file.load_file("mission_params.xml")) return -1;
    
    // Create a top level node object
    pugi::xml_node mission_params = 
          mission_params_file.child("ScamperParameters").child("MissionParameters");
    
    // Set all of the Mission Parameter properties
    
    // Home Station IP
    std::cout << "Reading Mission Parameters... "<< "\n";
    
    home_station_ip = mission_params.child("home_station_ip").text().get();
    if(home_station_ip && !home_station_ip[0]) return -1;
    std::cout <<"Home Station IP: " << home_station_ip << "\n";
    
    // TCP Port
    tcp_port = mission_params.child("tcp_port").text().get();
    if(tcp_port && !tcp_port[0]) return -1;
    std::cout << "TCP Port: " << tcp_port << "\n";
    
    // Serial Port
    serial_port = mission_params.child("serial_port").text().get();
    if(serial_port && !serial_port[0]) return -1;
    std::cout <<"Serial Port: " << serial_port << "\n";
    
    // Serial Baudrate
    serial_baudrate = mission_params.child("serial_baudrate").text().get();
    if(serial_baudrate && !serial_baudrate[0]) return -1;
    std::cout<< "Serial Baudrate: " << serial_baudrate << "\n";
    
    std::cout << "Success - Mission Parameters\n"
              << "|-----------------------------------------------------|\n";
    
    return 0;
}

MissionParameters::MissionParameters() {
}

MissionParameters::MissionParameters(const MissionParameters& orig) {
}

MissionParameters::~MissionParameters() {
}

