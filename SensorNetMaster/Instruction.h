/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Instruction.h
 * Author: antho_000
 *
 * Created on 21 August 2016, 16:34
 */

#ifndef INSTRUCTION_H
#define INSTRUCTION_H
#include<stdlib.h>
#include<string>
using namespace std;
class Instruction {
public:
    Instruction();
    Instruction(string type, std::string data);
    Instruction(const Instruction& orig);
    virtual ~Instruction();
    std::string GetData() const;
    string GetType() const;
    void SetData(std::string data);
    void SetType(string type);
private:
    string type;
    std::string data;
};

#endif /* INSTRUCTION_H */

