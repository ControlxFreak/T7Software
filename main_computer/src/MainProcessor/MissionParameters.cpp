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

#include "pugixml.hpp"
#include "MissionParameters.h"

// Read the Mission Parameters and Store them as parameters
int MissionParameters::read(){
    
    // Load and read the file
    pugi::xml_document mission_params_file;
    
    if(!mission_params_file.load_file('mission_params.xml')) return -1;
    
    // Create a top level node object
    pugi::xml_node mission_params = 
          mission_params_file.child("ScamperParameters").child("MissionParams");
    
    // Iterate through all of the attributes
    for (pugi::xml_node_iterator it = mission_params.begin(); 
                                 it != mission_params.end(); ++it){
        for (pugi)
        
    }
    
    
    
    
    return 0;    
}

MissionParameters::MissionParameters() {
}

MissionParameters::MissionParameters(const MissionParameters& orig) {
}

MissionParameters::~MissionParameters() {
}

