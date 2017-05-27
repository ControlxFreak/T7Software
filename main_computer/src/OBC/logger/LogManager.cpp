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
/* 
 * File:   LogManager.cpp
 * Author: controlxfreak
 * 
 * Created on April 8, 2017, 9:59 AM
 */

#include "LogManager.h"

//----------------------------------------------------------------------------//
// append() appends the input string into the buffer
//----------------------------------------------------------------------------//
void LogManager::append(string s)
{
    mutex_.lock();
    // Grab the current time and create the log filename
    auto now = Clock::now();
    time_t now_c = Clock::to_time_t(now);
    struct tm *parts = localtime(&now_c);
    int hour = parts->tm_hour;
    int min = parts->tm_min;
    int sec = parts->tm_sec;
    char tstamp[256];
    size_t sz = 255;
    snprintf(tstamp,sz,"%d:%d:%d:  ",hour,min,sec);
    buffer_<<tstamp<<s;
    mutex_.unlock();

    if(buffer_.str().size() >= maxBuffSize) print();
} // append()

//----------------------------------------------------------------------------//
// appendHeader() appends the header to the top of the buffer
//----------------------------------------------------------------------------//
void LogManager::appendHeader()
{
    mutex_.lock();
    buffer_ << "|-----------------------------------------------------|\n"
            << "Initializing Scamper Main On-Board Processor\n"
            << "Version : " << "0.12" << "\n"
            << "Build Date: " << "06 May 2017" << "\n"
            << "Built By: " << "Anthony" << "\n"
            << "|-----------------------------------------------------|\n"
            << "Lockheed Martin \n"
            << "Engineering Leadership Development Team \n"
            << "Team 7 \n"
            << "|-----------------------------------------------------|\n";
    mutex_.unlock();
    print();
} //appendHeader()

//----------------------------------------------------------------------------//
// clean() totally resets the buffer and opens a new file.  Not just clears it!
//----------------------------------------------------------------------------//
void LogManager::clean(){
    mutex_.lock();
    buffer_ << "Buffer Cleared.\n";
    mutex_.unlock();
    print();
    init();
}

//----------------------------------------------------------------------------//
// print() prints out the buffer to either the log file our the console or both
//----------------------------------------------------------------------------//
void LogManager::print(){

   mutex_.lock();
    
#ifdef PRINT_TO_CONSOLE
    cout<<buffer_.str();          
#endif
    
#ifdef PRINT_TO_FILE
    logFile.open(logFileName,ios::app);
    logFile << buffer_.str();
    logFile.close();
#endif
    
    // clear the buffer
    buffer_.str("");   
    
    mutex_.unlock();
}
//----------------------------------------------------------------------------//
// init() initializes the log file and file directory
//----------------------------------------------------------------------------//
void LogManager::init(){
    mutex_.lock();
    /*
    // Grab the current time and create the log filename
    auto now = Clock::now();
    time_t now_c = Clock::to_time_t(now);
    struct tm *parts = localtime(&now_c);
    int year = 1900 + parts->tm_year;
    int mon =  1 + parts->tm_mon;
    int day =  parts->tm_mday;
    int hour = parts->tm_hour;
    int min = parts->tm_min;
    
    
    // Grab our current path and append point it towards the logs directory subpath
    boost::filesystem::path curpath(boost::filesystem::current_path());
    string path = curpath.generic_string();
    string subpath;
    subpath = boost::str(boost::format("/logs/%d_%d_%d/") % year % mon % day);
    path.append(subpath);
    
    // Create the path if it does not exist already
    boost::filesystem::create_directory(path);    
    
    // Make the filename
    string s;
    s = boost::str(boost::format("%s%d_%d_%d_%d:%d_%d_logfile.log") %path % year % mon % day % hour %min % suffix_);
    logFileName = s;
    suffix_++;
    */
    mutex_.unlock();
} // init()

LogManager::LogManager() {
    init();
} //LogManager

LogManager::LogManager(const LogManager& orig) {
}

LogManager::~LogManager() {
}

