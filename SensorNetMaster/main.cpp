 
 
 /**Based on RF24Mesh_Example_Master.cpp by TMRh20
  */
  
#include <RF24Mesh/RF24Mesh.h> 
#include <RF24/RF24.h>
#include <RF24Network/RF24Network.h>
#include "LocalIO.h"

RF24 radio(RPI_V2_GPIO_P1_15, BCM2835_SPI_CS0, BCM2835_SPI_SPEED_8MHZ);  
RF24Network network(radio);
RF24Mesh mesh(radio,network);
LocalIO localIO;

//Function Declarations
bool sendNodeInstructions(int16_t nodeID);


int main(int argc, char** argv) {
  
  // Set the nodeID to 0 for the master node
  mesh.setNodeID(0);
  // Connect to the mesh
  printf("start\n");
  mesh.begin();
  radio.setPALevel(RF24_PA_MAX);
  radio.setDataRate(RF24_250KBPS);
 // radio.setChannel(80);
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
    
    float dat=0;
    int16_t nodeID = mesh.getNodeID(header.from_node);
    
    switch(header.type){
      //Temperature
      case 'T': network.read(header,&dat,sizeof(dat)); 
                printf("Temperature %f from %i\n",dat,nodeID);
                localIO.stringOut();
                break;
      //Humidity
      case 'H': network.read(header,&dat,sizeof(dat));
                printf("Humidity %f from %i\n",dat,nodeID);
                localIO.stringOut();
                break;
      //Instruction request
      case 'I': network.read(header,0,0);
                if(!sendNodeInstructions(nodeID)){
                  printf("!Failed to send instructions to %i\n", nodeID);
                }
                else printf("Instructions sent successfully to %i\n", nodeID);                   
                break;
      
      default:  network.read(header,0,0); 
                printf("Rcv bad type %hho from %i\n",header.type,nodeID); 
                break;
    }
  }
delay(2);
  }
return 0;
}

/**For now just sends empty instruction message to node. In future will check if
there are any instructions waiting to be sent and forward them**/
bool sendNodeInstructions(int16_t nodeID){
    int i = 10;
    bool sent = 0;
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
      
      
      
