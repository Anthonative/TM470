
/** Based on RF24Mesh_Example.ino by TMRh20
 *
 */

#include <Arduino.h>
#include <RF24.h>
#include <RF24Network.h>
#include <RF24Mesh.h>
#include <SPI.h>
#include <DHT.h>
#include <EEPROM.h>
#include <stdlib.h>
#include <Timer.h>
// imports the Serial class to allow log output
extern HardwareSerial Serial;

//Configure nRF24l01+
RF24 radio(7, 8);
RF24Network network(radio);
RF24Mesh mesh(radio, network);
//DHT
DHT dht(5, DHT22);
//Timer
Timer timer;
//Funcs
void send();
void receive();
//Attributes
long updatePeriod = 5000;
        
struct payload_t {
  unsigned long ms;
  unsigned long counter;
};

void setup() {
  //Serial begin
  Serial.begin(115200);
  mesh.setNodeID(10);
  //Set NodeID from serial
  while (!mesh.getNodeID()) {
    // Wait for the nodeID to be set via Serial
    if (Serial.available()) {
      Serial.print("NodeID unset. New NodeID?");
      mesh.setNodeID(Serial.parseInt());
      Serial.print("Set NodeID: ");
      Serial.println(mesh.getNodeID());
    }
  }
  //DHT begin
  dht.begin();
  //set timer to trigger update event
  int updateEvent = timer.every(updatePeriod, send);
  // Connect to the mesh
  Serial.println(F("Connecting to the mesh..."));
  mesh.begin();
  //radio.setChannel(80);
  radio.setDataRate(RF24_250KBPS);
  radio.setPALevel(RF24_PA_MAX);
  Serial.println("Connected");
  radio.powerDown();
}

void loop() {
    timer.update();
}

void send(){
    //get temp and humidity
    float temp = dht.readTemperature();
    
    float hum = dht.readHumidity();

    //power up radio, update mesh
    radio.powerUp();
    mesh.update();
    //Send temperature
    Serial.print("Sending temp: "); Serial.println(temp);
    if(!mesh.write(&temp, 'T', sizeof(temp))){
        if ( ! mesh.checkConnection() ) {
        //refresh the network address
        Serial.println("Renewing Address");
        mesh.renewAddress();
      } else {
        Serial.println("Send fail, Test OK");
      }
    };
    //Send humidity    
    Serial.println("Sending humidity: "); Serial.println(hum);
    if(!mesh.write(&hum, 'H', sizeof(hum))){
        if ( ! mesh.checkConnection() ) {
        //refresh the network address
        Serial.println("Renewing Address");
        mesh.renewAddress();
      } else {
        Serial.println("Send fail, Test OK");
      }
    };
    //Request instructions
    Serial.println("Requesting instructions");
    if(!mesh.write(0, 'I', 1)){
       if ( ! mesh.checkConnection() ) {
        //refresh the network address
        Serial.println("Renewing Address");
        mesh.renewAddress();
      } else {
        Serial.println("Send fail, Test OK");
      }
    } else receive();
    
}
//Receive instructions from master.
void receive(){
    int i = 10;
    bool received = 0;
    while(!received && i > 0){
        delay(2);
        radio.powerUp();
        if(network.available()){
            RF24NetworkHeader header;
            network.peek(header);
        
            switch(header.type){
             case 'I': network.read(header,0,0);
                  Serial.println("Message received, no instructions");
                  break; 
            }
            i = 0;
            received = 1;
        }
        radio.powerDown();
        --i;
    }
    if(!received) Serial.println("No instructions received");
    radio.powerDown();
}
void setUpdatePeriod(long a){
    updatePeriod = a;
}



