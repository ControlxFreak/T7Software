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
    20 March 2017 - t3 - Happy Birthday!
--------------------------------------------------------------------------------
 */

#ifndef TSQUEUE_H
#define TSQUEUE_H

#include <stdexcept>
#include <queue>
#include <string>
#include <vector>
#include "boost/thread.hpp"

using namespace std;

template <typename T>
class TSQueue {
public:
    
    
    int size();
    bool isEmpty();
    void push(T);
    T front();
    void pop();
    void clear();
        
    //------------------------------------------------------------------------//   
    // Constructor and Destructor
    //------------------------------------------------------------------------//
    TSQueue();
    TSQueue(const TSQueue& orig);
    ~TSQueue();

protected:
    queue<T> data_;
    boost::mutex mutex_;
private:

};

#endif /* TSQUEUE_H */

