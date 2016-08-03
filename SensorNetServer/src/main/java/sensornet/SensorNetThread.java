/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import static sensornet.Application.NODE_MAP_PATH;
import static sensornet.Application.INFIFO_PATH;

/**
 *
 * @author antho_000
 */

public class SensorNetThread implements Runnable{
    /**
     * @param args the command line arguments
     */
    
    private NodeMap nodeMap;
    private final AtomicLong counter = new AtomicLong();
    
    public SensorNetThread(NodeMap nodeMap){
        this.nodeMap = nodeMap;
        saveNodeMap();
    }
            
    @Override
    public void run() {
        BufferedReader in;
        System.out.println("Opening inFIFO");
        try{
            in = new BufferedReader(new FileReader(INFIFO_PATH));
            System.out.println("Open");
            
            while(true){
                if(in.ready()){
                    try{
                        readValues(in);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    saveNodeMap();
                }
            }
        }
        catch(IOException e){
            System.out.println("ERROR: Could not open input FIFO" + e);
            System.exit(-1);
        }
        
    }
    
    private void readValues(BufferedReader in) throws Exception{
        try{
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
              nodeMap.getNode(nodeID).addValue(LocalDateTime.now(), type, value);
              System.out.println("New value recieved.");
              System.out.println(LocalDateTime.now());
             // printLastValues();
         }
        }
        catch(IOException e){
            System.out.println("A thing went wrong");
        }
    }
    
    
    
    
    
    private void saveNodeMap(){
        try{
                FileOutputStream fileOut = new FileOutputStream(NODE_MAP_PATH);
                ObjectOutputStream nodeMapOut = new ObjectOutputStream(fileOut);
                nodeMapOut.writeObject(nodeMap);
                nodeMapOut.close();
                fileOut.close();
                System.out.println("Node map saved.");
            }
            catch(IOException e){
                e.printStackTrace();
            }
    }
    
    public void printLastValues(){
        System.out.println(stringLastValues());
    }

     public String stringLastValues(){
        String out = "Most recent values:\n";
        for (Map.Entry<Integer,NodeSerializable> nodeEntry : nodeMap.getNodeMap().entrySet()) {
            NodeSerializable node = nodeEntry.getValue();
            for(Map.Entry<String, SensorValueSerializable> valueEntry: node.getLastValues().entrySet()){
                SensorValueSerializable value = valueEntry.getValue();
                out = out + node.getName() + " : " + value.getTime() + " : " 
                        + value.getType() + " : " + value.getValue() + "\n";
            }
        }
        return out;
    }
   
    
    
}
