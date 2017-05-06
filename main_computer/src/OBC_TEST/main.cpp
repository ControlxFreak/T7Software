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

    // Keep trying to connect!        // Initialize the TCP connector
    connector = new TCPConnector();

    stream = connector->connect("127.0.0.1",9001+2);
    
    // Check for thread interruptions
    string buff;
    GM.set_msgtype((google::protobuf::int32) 2);
    GM.set_time((google::protobuf::int32) 0);
    GM.mutable_terminate()->set_terminatekey((google::protobuf::int32) 0);
    GM.SerializeToString(&buff);
    stream->send(buff.c_str(),buff.length());
    
    return;
}

// test_tcp does what it sounds like...
void test_tcp(int id){
     // Verify that the version of the library that we linked against is
    // compatible with the version of the headers we compiled against.
    GOOGLE_PROTOBUF_VERIFY_VERSION;
    
    // Initialize the TCP Classes
    TCPStream* stream;
    TCPAcceptor* acceptor;
    TCPConnector* connector;
        
    // Initialize the Message
    T7::GenericMessage GM;
    
    // Initialize the TCP stream depending on if it is a server or client connection
    if(id <= 200) // then it is a client
    {
        // Initialize the TCP connector
        connector = new TCPConnector();
        
        stream = connector->connect("127.0.0.1",9001+id);      
    }else{
        // Initialize the TCP acceptor
        acceptor = new TCPAcceptor(9001+id,"127.0.0.1");   
        
        // Keep trying to accept!
         stream = acceptor->accept();
    } //else
     // Loop through until you we're done!
    bool timeToDie = false;
    string buff;
    while(!timeToDie)
    {
        // Clear the generic message for this round
        GM.Clear();
        
        // Switch based on the sock id
        switch(id){
            case 1: 
                // Check for thread interruptions
               GM.set_msgtype((google::protobuf::int32) id);
               GM.set_time((google::protobuf::int32) 0);
               GM.mutable_heartbeat()->set_alive(false);
               GM.SerializeToString(&buff);
               stream->send(buff.c_str(),buff.length());
               timeToDie = true;
               break;
            case 2:
                send_terminate();
            case 101:
                // Check for thread interruptions
                GM.set_msgtype((google::protobuf::int32) id);
                GM.set_time((google::protobuf::int32) 0);
                GM.mutable_configdata()->set_configkey((google::protobuf::int32) 0);
                GM.SerializeToString(&buff);
                stream->send(buff.c_str(),buff.length());
                timeToDie = true;
                break;
            case 102:
                // Check for thread interruptions
                GM.set_msgtype((google::protobuf::int32) id);
                GM.set_time((google::protobuf::int32) 0);
                GM.mutable_movecamera()->set_arrowkey((google::protobuf::int32) 0);
                GM.SerializeToString(&buff);
                stream->send(buff.c_str(),buff.length());
                timeToDie = true;
                break;
            default: 
                cout<<"Unsupported Command!\n";
                timeToDie = true;
                break;
        } //switch
    } //while(!timeToDie)
    
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
               int id;
               cout <<"Enter TCP ID: \n";
               cout <<"1 Heartbeat\n";
               cout <<"2 Terminate\n";
               cout <<"101 Config Data\n";
               cout <<"102 Move Camera\n";
               cout <<"200 Accel\n";
               cout <<"201 Gyro\n";
               cout <<"202 Alt\n";
               cout <<"203 Att\n";
               cout <<"204 Temp\n";
               cout <<"205 Bat\n";
               cout <<"999 ALL\n";
               cin >> id;
               test_tcp(id);
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