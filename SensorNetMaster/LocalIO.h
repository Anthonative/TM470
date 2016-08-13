/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   LocalIO.h
 * Author: antho_000
 *
 * Created on 24 July 2016, 17:41
 */
#include <stdint.h>
#include <string>
#include <stdlib.h>

#ifndef LOCALIO_H
#define LOCALIO_H
class LocalIO {
public:
    LocalIO();
    LocalIO(char const* path);
    virtual ~LocalIO();
    void stringOut(char* output[]);
private:
    char const* fifoPath;
};

#endif /* LOCALIO_H */

