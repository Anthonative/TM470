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
#include<inttypes.h>
#include<queue>
#include<map>
#include<stdlib.h>
#include<stdio.h>
#include<iostream>
#include<fstream>
#include<Instruction.h>
using namespace std;
#ifndef INSTRUCTIONMANAGER_H
#define INSTRUCTIONMANAGER_H

class InstructionManager {
public:
    InstructionManager(char const* path);
    virtual ~InstructionManager();
    void addNode(string nodeID);
    void addInstruction(string nodeID, Instruction* instruction);
    std::queue<Instruction*>* getInstructions(string nodeID);
    bool hasInstructions(string nodeID);
    void readInstructions();
    void parseInstructionString(std::string instructionString);
private:
    std::map<std::string,std::queue<Instruction*> > instructionMap;
    char const* fifoPath;
    ifstream inStream;
};

#endif /* INSTRUCTIONMANAGER_H */

