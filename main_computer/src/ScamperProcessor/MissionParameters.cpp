/*------------------------------------------------------------------------------
Function Name: MissionParameters.cpp
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

#include "MissionParameters.h"

// Read the Mission Parameters and Store them as parameters
void MissionParameters::read(){
    
 
    // Init the loop variables
    std::string nodename;
    pugi::xml_node node;
    
    
    // Load and read the file
    pugi::xml_document mission_params_file;
    if(!mission_params_file.load_file("mission_params.xml")) fail();
    
    // Create a top level node object
    pugi::xml_node mission_params = 
          mission_params_file.child("ScamperParameters").child("MissionParameters");
    
    // Loop through the nodes
    for (pugi::xml_node_iterator it = mission_params.begin(); it != mission_params.end(); ++it)
    {
        // Cast the name to a string
        nodename = it->name();
        
        // Check the name and set it to its appropriate value
        if (nodename == "Communication"){
            node = mission_params.child("Communication");
            readComParams(node);
        }
        else if(nodename == "SensorData"){
            node = mission_params.child("SensorData");
            readSensorParams(node);
        }
        else{
            std::cout<< "Unknown node name: " << nodename << std::endl;
        }
        
    } // top level
  
}
  
void MissionParameters::fail(){
    std::cout<<"Failed to load the Mission Parameters";
    exit(EXIT_FAILURE);
}

    
void MissionParameters::readComParams(pugi::xml_node comnode){
    
    // Initialize the string
    std::string str;
    
    // TCP
    // Receive
    str = (std::string)comnode.child("tcp_rec_port").text().get();
    if(str.empty()) fail();
    ComParams.tcp_rec_port = stoi(str);
    std::cout << "TCP Receive Port: " << ComParams.tcp_rec_port << "\n";
    str.clear();
    
    // Send
    str = (std::string)comnode.child("tcp_sen_port").text().get();
    if(str.empty()) fail();
    ComParams.tcp_sen_port = stoi(str);
    std::cout << "TCP Send Port: " << ComParams.tcp_sen_port << "\n";
    str.clear();
    
    // Serial
    // Port Number
    str = (std::string)comnode.child("serial_port").text().get();
    if(str.empty()) fail();
    ComParams.serial_port = stoi(str);
    std::cout << "Serial Port: " << ComParams.serial_port << "\n";
    str.clear();
    
    // Baud Rate
    str = (std::string)comnode.child("serial_baudrate").text().get();
    if(str.empty()) fail();
    ComParams.serial_baudrate = stoi(str);
    std::cout << "Serial Baud Rate: " << ComParams.serial_baudrate << "\n";
    str.clear();    
    
}

void MissionParameters::readSensorParams(pugi::xml_node sensornode){
        
    // Sensor Data
    // Altitude
    std::string str;
    
    str = (std::string)sensornode.child("altitude").text().get();
    if(str.empty()) fail();
    SensorParams.altitude = to_bool(str);
    std::cout << "Altitude: " << SensorParams.altitude << "\n";
    str.clear();
    
    // Optical
    str = (std::string)sensornode.child("optical").text().get();
    if(str.empty()) fail();
    SensorParams.optical = to_bool(str);
    std::cout << "Optical: " << SensorParams.optical << "\n";
    str.clear();
}

MissionParameters::MissionParameters() {
}

MissionParameters::MissionParameters(const MissionParameters& orig) {
}

MissionParameters::~MissionParameters() {
}

bool MissionParameters::to_bool(std::string str) {
    std::transform(str.begin(), str.end(), str.begin(), ::tolower);
    std::istringstream is(str);
    bool b;
    is >> std::boolalpha >> b;
    return b;
}
