/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.cpp
 * Author: controlxfreak
 *
 * Created on May 5, 2017, 5:47 PM
 */

#include <cstdlib>
#include <iostream>
#include "../OBC/io/T7Messages.pb.h"
#include "tcpacceptor.h"
#include "tcpconnector.h"
#include "tcpstream.h"

using namespace std;

// test_tcp does what it sounds like...
void test_tcp(){
    return;
}
                        
// send_terminate does what it sounds like...
void send_terminate(){
    
    // Verify that the version of the library that we linked against is
    // compatible with the version of the headers we compiled against.
    GOOGLE_PROTOBUF_VERIFY_VERSION;
    
    // Initialize the TCP Classes
    TCPStream* stream;
    TCPConnector* connector;
    
    // Initialize the Message
    T7::GenericMessage GM;
    
    // Initialize the TCP connector
    connector = new TCPConnector();

    // Keep trying to connect!
    stream = connector->connect("127.0.0.1",9001,3000);
    // Check for thread interruptions
    usleep(3000);
    string buff;
    GM.set_msgtype((google::protobuf::int32) 2);
    GM.set_time((google::protobuf::int32) 0);
    GM.mutable_terminate()->set_terminatekey((google::protobuf::int32) 0);
    GM.SerializeToString(&buff);
    stream->send(buff.c_str(),buff.length());
    
    return;
}

int main(int argc, char** argv) {

   cout << "Welcome to the T7 OBC Test Environment.\n";
   int rply = 0;
   while(rply != -1)
   {
       cout << "What do you want to do?\n";
       cout << "Enter a command:\n";
       cout << "1 Test TCP\n";
       cout << "2 Exit Test\n";
       cout << "3 Terminate OBC\n";
       cin >> rply;
       switch(rply)
       {
           case 1: 
               test_tcp();
               break;
           case 2:
               return 0;
           case 3:
               send_terminate();
               break;
           default:
               cout << "Unidentified Command!";
               break;
       }
       cout << "|---------------------------|\n";
   }
    return 0;
}