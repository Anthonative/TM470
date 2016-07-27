/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   SensorValue.h
 * Author: antho_000
 *
 * Created on 25 July 2016, 16:54
 */
#include<stdint.h>
#include<string>

#ifndef SENSORVALUE_H
#define SENSORVALUE_H

class SensorValue {
public:
    SensorValue();
    SensorValue(char type, uint8_t nodeID, float value);
    virtual ~SensorValue();
    void SetValue(float value);
    float GetValue() const;
    void SetNodeID(uint8_t nodeID);
    uint8_t GetNodeID() const;
    void SetType(char type);
    char GetType() const;
    std::string ToString();
private:
    char type;
    uint8_t nodeID;
    float value;
};

#endif /* SENSORVALUE_H */

