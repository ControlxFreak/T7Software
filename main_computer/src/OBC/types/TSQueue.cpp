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
#include "TSQueue.h"

//------------------------------------------------------------------------//
// Clear the data from the queue //
//------------------------------------------------------------------------//
template<typename T>
void TSQueue<T>::clear(){
    boost::lock_guard<boost::mutex> guard(mutex_);
    queue<T> empty;
    swap(data_,empty);
}

//------------------------------------------------------------------------//
// Size of the thread safe queue //
//------------------------------------------------------------------------//
template<typename T>
int TSQueue<T>::size(){
    boost::lock_guard<boost::mutex> guard(mutex_);
    return data_.size();
};

//------------------------------------------------------------------------//    
// Return if the thread safe queue is empty or not //   
//------------------------------------------------------------------------//
template<typename T>
bool TSQueue<T>::isEmpty(){
    boost::lock_guard<boost::mutex> guard(mutex_);
    return data_.empty();
};

//------------------------------------------------------------------------//    
// Return if the thread safe queue is empty or not //   
//------------------------------------------------------------------------//
template<typename T>
void TSQueue<T>::push(T elem){
    boost::lock_guard<boost::mutex> guard(mutex_);
    data_.push(elem);
};

//------------------------------------------------------------------------//    
// Get the next element from the queue
//------------------------------------------------------------------------//
template<typename T>
T TSQueue<T>::front(){
    boost::lock_guard<boost::mutex> guard(mutex_);
    if(data_.empty()) throw out_of_range("No elements in the queue");
    return data_.front();
};
//------------------------------------------------------------------------//    
// Pop the next element in the queue
//------------------------------------------------------------------------//
template<typename T>
void TSQueue<T>::pop()
{
    boost::lock_guard<boost::mutex> guard(mutex_);
    if(data_.empty()) throw out_of_range("No elements in the queue");
    data_.pop();   
}

template<typename T>
TSQueue<T>::TSQueue()
{
    
}
template<typename T>
TSQueue<T>::~TSQueue()
{
    
}

template class TSQueue<int>;
template class TSQueue<float>;
template class TSQueue<double>;
template class TSQueue<string>;

template class TSQueue< vector<int> >;
template class TSQueue< vector<float> >;
template class TSQueue< vector<double> >;
template class TSQueue< vector<string> >;