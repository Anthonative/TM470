/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   SensorValue.cpp
 * Author: antho_000
 * 
 * Created on 25 July 2016, 16:54
 */

#include "SensorValue.h"
#include "LocalIO.h"
#include<string.h>
#include<cstdio>
#include <sstream>
    

SensorValue::SensorValue(char type, uint8_t nodeID, float value) {
    SetType(type);
    SetNodeID(nodeID);
    SetValue(value);
}

std::string SensorValue::ToString(){
    std::string out;
    std::stringstream ostream;
    ostream << (int)nodeID << ',' << type << ',' << value;
    out = ostream.str();
    return out;
}

SensorValue::~SensorValue() {
}

void SensorValue::SetValue(float value) {
    this->value = value;
}

float SensorValue::GetValue() const {
    return value;
}

void SensorValue::SetNodeID(uint8_t nodeID) {
    this->nodeID = nodeID;
}

uint8_t SensorValue::GetNodeID() const {
    return nodeID;
}

void SensorValue::SetType(char type) {
    this->type = type;
}

char SensorValue::GetType() const {
    return type;
}

