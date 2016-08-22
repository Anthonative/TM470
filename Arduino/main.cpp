
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
#define NODE_ID 5

//DHT
DHT dht(5, DHT22);
//Timer
Timer timer;
//Funcs
void send();
void receive();
void sendValueString(String);
void setUpdatePeriod(int);
//Attributes
int updatePeriod = 5000;
int updateEvent;
        
struct payload_t {
  unsigned long ms;
  unsigned long counter;
};

void setup() {
  //Serial begin
  Serial.begin(115200);
  mesh.setNodeID(NODE_ID);
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
  updateEvent = timer.every(updatePeriod, send);
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
    //send temp and humidity
    float temperature = dht.readTemperature();
    char temp[10];
    dtostrf(temperature,3,2,temp);
    String temperatureString = String(mesh.getNodeID()) + ";Temperature;";
    temperatureString = temperatureString + temp;
    sendValueString(temperatureString);

    float humidity = dht.readHumidity();
    char hum[10];
    dtostrf(humidity,3,2,hum);
    String humidityString = String(mesh.getNodeID()) + ";Humidity;";
    humidityString = humidityString + hum;
    sendValueString(humidityString);
    //power up radio, update mesh
    radio.powerUp();
    mesh.update();
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

void sendValueString(String out){
    radio.powerUp();
    char outChar[out.length() + 1];
    out.toCharArray(outChar, out.length() + 1);
    Serial.print("Sending: "); Serial.println(outChar);
    if(!mesh.write(outChar, 'V', sizeof(outChar))){
        if ( ! mesh.checkConnection() ) {
        //refresh the network address
        Serial.println("Renewing Address");
        mesh.renewAddress();
      } else {
        Serial.println("Send fail, Test OK");
      }
    };
    radio.powerDown();
}

//Receive instructions from master.
void receive(){
    int i = 100;
    bool received = 0;
    radio.powerUp();
    while(!received && i > 0){
        if(network.available()){
            RF24NetworkHeader header;
            network.peek(header);
            int8_t len = radio.getDynamicPayloadSize();
            switch(header.type){
             case 'I':{ 
                        network.read(header,0,0);
                        Serial.println("Message received, no instructions");
                        break; 
                      }
             case 'U':{
                        char value[len];
                        network.read(header,&value,sizeof(value));
                        String inString(value);
                        Serial.println("Received: " + inString);
                        int period = inString.toInt();
                        Serial.println(period);
                        setUpdatePeriod(period);
                        Serial.println("Update frequency changed to " + period);
                        break;
                      }
             }
            i = 0;
            received = 1;
        }
        --i;
        delay(2);
    }
    if(!received) Serial.println("No instructions received");
    radio.powerDown();
}
void setUpdatePeriod(int a){
    updatePeriod = a;
    timer.stop(updateEvent);
    updateEvent = timer.every(updatePeriod, send);
}



