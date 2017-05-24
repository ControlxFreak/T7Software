/*------------------------------------------------------------------------------
Function Name: LogManager.h

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

#ifndef LOGMANAGER_H
#define LOGMANAGER_H
#include <string>
#include <sstream>
#include <fstream>
#include <iostream>
#include <chrono>
#include <thread>
#include <mutex>
//#include "boost/date_time.hpp"
//#include "boost/format.hpp"
//#include "boost/filesystem.hpp"

using namespace std;

typedef chrono::system_clock Clock;

class LogManager {
public:
    
    void append(string s);
    void appendHeader();
    void init();
    void clean();
    
    static LogManager* getInstance()
    {
        static LogManager* p_LogManager = new LogManager();
        return p_LogManager;
    };    
    
    LogManager();
    LogManager(const LogManager& orig);
    virtual ~LogManager();
private:
    
    
    int maxBuffSize = 1;
    stringstream buffer_ = stringstream();
    mutex mutex_;
    ofstream logFile;
    string logFileName;
    int suffix_ = 0;
    
    void print();
};

#endif /* LOGMANAGER_H */

