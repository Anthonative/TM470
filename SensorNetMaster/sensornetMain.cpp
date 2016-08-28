 
 
 /**Based on RF24Mesh_Example_Master.cpp by TMRh20
  */
  
#include <RF24Mesh/RF24Mesh.h> 
#include <RF24/RF24.h>
#include <RF24Network/RF24Network.h>
#include "LocalOut.h"
#include "InstructionManager.h"
#include <sstream>
#include <string>
#include "Instruction.h"

#define VALUESOUT "/etc/sensornet/sensorvalues"
#define INSTRUCTIONSIN "/etc/sensornet/instructions"

LocalOut valuesOut(VALUESOUT);
InstructionManager instructionManager(INSTRUCTIONSIN);

RF24 radio(RPI_V2_GPIO_P1_15, BCM2835_SPI_CS0, BCM2835_SPI_SPEED_8MHZ);  
RF24Network network(radio);
RF24Mesh mesh(radio,network);

//Function Declarations
bool sendNodeInstructions(int16_t nodeID);


int main(int argc, char** argv) {
  // Set the nodeID to 0 for the master node
  mesh.setNodeID(0);
  // Connect to the mesh
  printf("start\n");
  mesh.begin();
  //radio.setChannel(80);
  radio.setPALevel(RF24_PA_MAX);
  radio.setDataRate(RF24_250KBPS);
  radio.printDetails();
 

while(1)
{

  // Call network.update as usual to keep the network updated
  mesh.update();
  // In addition, keep the 'DHCP service' running on the master node so addresses will
  // be assigned to the sensor nodes
  mesh.DHCP();
  // Check for incoming data from the sensors
  while(network.available()){
    RF24NetworkHeader header;
    network.peek(header);
    
    int16_t nodeID = mesh.getNodeID(header.from_node);
    char type = header.type;
    int8_t len = radio.getDynamicPayloadSize();
    printf("Message received, length: %i\n",len);
    switch(type){
      //Value
      case 'V': {
          printf("V\n");
                char* value[len];
                network.read(header,&value,sizeof(value));
                printf("%s\n",value);
                valuesOut.stringOut(value);
                break;
                }
      //Instruction request
      case 'I': {
                network.read(header,0,0);
                if(!sendNodeInstructions(nodeID)){
                  printf("!Failed to send instructions to %hi\n", nodeID);
                }
                else printf("Instructions sent successfully to %hi\n", nodeID);                   
                break;
                }
      
      default:  {
                network.read(header,0,0); 
                printf("Rcv bad type %hho from %hi\n",header.type,nodeID); 
                break;
                }
    }
  }
//Check for incoming instructions from server
  instructionManager.readInstructions();  
  //delay(2);
 }
return 0;
}

/**For now just sends empty instruction message to node. In future will check if
there are any instructions waiting to be sent and forward them**/
bool sendNodeInstructions(int16_t nodeID){
    stringstream ss;
    ss << nodeID;
    bool sent = 0;
    if(!instructionManager.hasInstructions(ss.str())){
        int i = 100;
        while(i > 0){
            if(mesh.write(0,'I',0,nodeID)){
              i = 0;
              sent = 1;
            }
            --i;
            delay(2);
        }
      return sent;
    }
    std::queue<Instruction*>* instructions = instructionManager.getInstructions(ss.str());
    int a = 10;
    while(!instructions->empty() && a>0){
        Instruction* instruction = instructions->front();
        char type = instruction->GetType().c_str()[0];
        string data = instruction->GetData();
        char dataChar[data.length()+1];
        strncpy(dataChar,data.c_str(), data.length()+1);
        dataChar[data.length()] = '\0';
        int i = 100;
        while(i > 0){
                if(mesh.write(dataChar,type,sizeof(dataChar),nodeID)){
                instructions->pop();
                sent = 1;
                i=0;
                }
            i--;
            mesh.update();
            mesh.DHCP();
            delay(2);
        }
        a--;
    }
    return sent;
}      
      
      
      
