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
#include "Instruction.h"
#include <stdlib.h>
#include <stdio.h>
#include <stdint.h> 
#include <iostream>
#include <fstream>
#include <map>
#include <queue>
#include <iosfwd>
#include <sstream>
using namespace std;

InstructionManager::InstructionManager(char const* path) {
    fifoPath = path;
    printf("Opening inFIFO\n");
    inStream.open(fifoPath, ifstream::in);
}


InstructionManager::~InstructionManager() {
}

void InstructionManager::addNode(string nodeID){
    std::queue<Instruction*> queue;
    instructionMap.insert(std::pair<std::string,std::queue<Instruction*> >(nodeID,queue));
}

void InstructionManager::addInstruction(string nodeID, Instruction* instruction){
    if(instructionMap.count(nodeID) < 1){
        addNode(nodeID);
    }
    instructionMap.at(nodeID).push(instruction);
}

 std::queue<Instruction*>* InstructionManager::getInstructions(string nodeID){
    return &instructionMap.at(nodeID);
}

bool InstructionManager::hasInstructions(string nodeID){
    if(instructionMap.count(nodeID) < 1) return false;
    return(instructionMap[nodeID].size() > 0);
}

void InstructionManager::readInstructions(){
    string instruction;
    inStream.open(fifoPath);
    while(std::getline(inStream,instruction)){
        //printf("instructionloop\n");
        if(instruction == "END")break;
        parseInstructionString(instruction);
    }
    inStream.close();
    //printf("Finished instructions\n");
}

void InstructionManager::parseInstructionString(std::string instructionString){
    stringstream ss(instructionString);
    string token;
    vector<string> tokens;
    while(getline(ss, token, ';')){
        tokens.push_back(token);
        printf("%s\n",token.c_str());
    }
    if(tokens.size()==3){
        string nodeID;
        nodeID = tokens.at(0);
        string type = tokens.at(1);
        string data = tokens.at(2);
        Instruction *instruction = new Instruction(type, data);
        addInstruction(nodeID,instruction);
    }
            
}
