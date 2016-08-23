/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static sensornet.Application.NODE_MAP_PATH;
import static sensornet.Application.INFIFO_PATH;
import static sensornet.Application.INSTRUCTIONS_PATH;

/**
 *
 * @author antho_000
 */

public class SensorNetThread implements Runnable{
    /**
     * @param args the command line arguments
     */
    
    private NodeMap nodeMap;
    
    public SensorNetThread(NodeMap nodeMap){
        this.nodeMap = nodeMap;
        //test    
            nodeMap.removeNode(100);
            nodeMap.addNode(100);
            for(int i=0; i<10000; i++){
                nodeMap.getNode(100).addValue(LocalDateTime.now().plusMinutes(i), "Test", i);
            }
        
        nodeMap.save();
    }
            
    @Override
    public void run() {
        BufferedReader in;
        BufferedOutputStream out;
        try{
            System.out.println("Opening inFIFO");
            in = new BufferedReader(new FileReader(INFIFO_PATH));
            System.out.println("Open");
            while(true){
                if(in.ready()){
                    readValues(in);
                    nodeMap.save();
                }
                sendInstructions();
            }
        }
        catch(Exception e){
            System.out.println("ERROR: ");
            e.printStackTrace();
            System.exit(-1);
        }
        
    }
    
    private void readValues(BufferedReader in) throws Exception{
            while(in.ready()){
              System.out.println("Reading next value");
              String valueString = in.readLine();
              String[] split = valueString.split(";");
                if (split.length != 3){
                    Exception e = new Exception("Invalid sensor value string from mesh" + valueString);
                    throw e;
                }
              int nodeID = Integer.parseInt(split[0]);
              String type = split[1];
              double value = Double.parseDouble(split[2]);
              //add node to node map if not already known
              if(!nodeMap.hasNode(nodeID)){
                  nodeMap.addNode(nodeID);
              }
              nodeMap.getNode(nodeID).addValue(LocalDateTime.now(), type, value);
              System.out.println("New value recieved.");
              System.out.println(LocalDateTime.now());
             // printLastValues();
         }
    }
    
    private void sendInstructions() throws IOException{
            BufferedOutputStream instructionsOut = new BufferedOutputStream(new FileOutputStream(INSTRUCTIONS_PATH));
            List<String> insructionList = nodeMap.getInstructionList();
            PrintWriter printWriter = new PrintWriter(instructionsOut);
            for(String instruction : insructionList){
                printWriter.println(instruction);
                System.out.println(instruction);
            }
            nodeMap.getInstructionList().clear();
            printWriter.println("END");
            printWriter.close();
    }
    
    
    
    
    
    public void printLastValues(){
        System.out.println(stringLastValues());
    }

     public String stringLastValues(){
        String out = "Most recent values:\n";
        for (Map.Entry<Integer,Node> nodeEntry : nodeMap.getNodeMap().entrySet()) {
            Node node = nodeEntry.getValue();
            for(Map.Entry<String, SensorValue> valueEntry: node.getLastValues().entrySet()){
                SensorValue value = valueEntry.getValue();
                out = out + node.getName() + " : " + value.getTime() + " : " 
                        + value.getType() + " : " + value.getValue() + "\n";
            }
        }
        return out;
    }
   
    
    
}
