/* 
 * File:   LocalIO.cpp
 * Author: Anthony Jones
 * 
 * Created on 25 June 2016, 21:55
 */

#include "LocalIO.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <vector>
using namespace std;


FILE *outFIFO;
FILE *inFIFO;

LocalIO::LocalIO() {
}

LocalIO::LocalIO(char const* path){
    fifoPath = path;
}


LocalIO::~LocalIO(){
    
}

void LocalIO::stringOut(char* output[]){
  outFIFO = fopen (fifoPath,"w");
  if(outFIFO == NULL){
      printf("ERROR: Could not open output FIFO\n");
      exit(EXIT_FAILURE);
  }
 printf("%i\n",fprintf(outFIFO,"%s\n",output));
 fclose(outFIFO);
}

