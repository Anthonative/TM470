/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   InstructionManager.h
 * Author: antho_000
 *
 * Created on 13 August 2016, 12:19
 */
#include<list>
#include<inttypes.h>
#include<queue>
#include<map>
#include<stdlib.h>
#include<stdio.h>
#include<iostream>
#include<fstream>
using namespace std;
#ifndef INSTRUCTIONMANAGER_H
#define INSTRUCTIONMANAGER_H

class InstructionManager {
public:
    InstructionManager(char const* path);
    virtual ~InstructionManager();
    void addNode(uint8_t nodeID);
    void addInstruction(uint8_t nodeID, char const* instruction);
    std::queue<char const*> getInstructions(uint8_t nodeID);
    bool hasInstructions(uint8_t nodeID);
    void readInstructions();
//    void parseInstructionString(std::string instruction);
private:
    std::map<uint8_t,std::queue<char const*> > instructionMap;
    char const* fifoPath;
    ifstream inStream;
};

#endif /* INSTRUCTIONMANAGER_H */

