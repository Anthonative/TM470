/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   InstructionManager.cpp
 * Author: antho_000
 * 
 * Created on 13 August 2016, 12:19
 */

#include "InstructionManager.h"
#include <stdlib.h>
#include <stdio.h>
#include <stdint.h> 
#include <iostream>
#include <fstream>
#include <map>
#include<queue>
#include <iosfwd>
#include <boost/algorithm/string.hpp>
using namespace std;

InstructionManager::InstructionManager(char const* path) {
    instructionMap = std::map<uint8_t,std::queue<char const*> >();
    fifoPath = path;
    printf("Opening inFIFO\n");
    inStream.open(fifoPath, ifstream::in);
}


InstructionManager::~InstructionManager() {
}

void InstructionManager::addNode(uint8_t nodeID){
    std::queue<char const*> queue = std::queue<char const*>();
    instructionMap[nodeID] = queue;
}

void InstructionManager::addInstruction(uint8_t nodeID, char const* instruction){
    if(instructionMap.count(nodeID) < 1){
        addNode(nodeID);
    }
    instructionMap[nodeID].push(instruction);
}

std::queue<char const*> InstructionManager::getInstructions(uint8_t nodeID){
    return instructionMap[nodeID];
}

bool InstructionManager::hasInstructions(uint8_t nodeID){
    if(instructionMap.count(nodeID) < 1) return false;
    return(instructionMap[nodeID].size() > 0);
}

void InstructionManager::readInstructions(){
    string instruction;
    inStream.open(fifoPath);
    while(std::getline(inStream,instruction)){
        printf("instructionloop\n");
        printf("Instruction: %s\n", instruction.c_str());
        if(instruction == "END")break;
    }
    inStream.close();
    printf("Finished instructions\n");
}

/*void InstructionManager::parseIntructionString(std::string instruction){
    
}*/
