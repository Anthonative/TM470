/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Instruction.cpp
 * Author: antho_000
 * 
 * Created on 21 August 2016, 16:35
 */

#include "Instruction.h"
#include<stdlib.h>
#include<string>
#include<stdlib.h>
#include<stdio.h>
using namespace std;

Instruction::Instruction() {
}

Instruction::Instruction(string type, std::string data){
    printf("create instruction\n");
    this->type = type;
    printf("%s\n",this->GetType().c_str());
    this->data = data;
    printf("%s\n",this->GetData().c_str());
}

Instruction::Instruction(const Instruction& orig) {
}

Instruction::~Instruction() {
}

std::string Instruction::GetData() const{
    return data;
}

std::string Instruction::GetType() const{
    return type;
}

