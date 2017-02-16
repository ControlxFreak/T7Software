/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   IOProcessor.h
 * Author: controlxfreak
 *
 * Created on February 15, 2017, 7:20 PM
 */

#ifndef IOPROCESSOR_H
#define IOPROCESSOR_H

class IOProcessor {
public:
    IOProcessor();
    IOProcessor(const IOProcessor& orig);
    virtual ~IOProcessor();
    int establish_tcp_connection();
    int establish_serial_connection();
    
private:

};

#endif /* IOPROCESSOR_H */

