/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornetsimple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author antho_000
 */
public class SensorNetSimple {
    /**
     * @param args the command line arguments
     */
    private static final String INFIFO = "/etc/sensornet/sensorvalues";
    private static final String NODEMAP = "/etc/sensornet/nodemap.ser";
    private static NodeMap nodeMap;
            
    public static void main(String[] args) {
        init();
        BufferedReader in;
        System.out.println("Opening inFIFO");
        try{
            in = new BufferedReader(new FileReader(INFIFO));
            System.out.println("Open");
            
            while(true){
                if(in.ready()){
                    try{
                        readValues(in);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(IOException e){
            System.out.println("ERROR: Could not open input FIFO" + e);
            System.exit(-1);
        }
        
    }
    
    private static void readValues(BufferedReader in) throws Exception{
        try{
            while(in.ready()){
              System.out.println("Reading next value");
              String valueString = in.readLine();
              String[] split = valueString.split(";");
                if (split.length != 3){
                    Exception e = new Exception("Invalid sensor value string from mesh");
                    throw e;
                }
              int nodeID = Integer.parseInt(split[0]);
              String type = split[1];
              double value = Double.parseDouble(split[2]);
              nodeMap.getNode(nodeID).addValue(LocalDateTime.now(), type, value);
              System.out.println("New value recieved.");
              System.out.println(LocalDateTime.now());
              printLastValues();
         }
        }
        catch(IOException e){
            System.out.println("A thing went wrong");
        }
    }
    
    private static void init(){
        File nodeMapFile = new File(NODEMAP);
        if(!nodeMapFile.isFile()){
            try{
            nodeMapFile.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            nodeMap = new NodeMap();
            saveNodeMap();
        }
        else{
            openNodeMap();
        }
    }
    
    private static void openNodeMap(){
        try{
            FileInputStream fileIn = new FileInputStream(NODEMAP);
            ObjectInputStream nodeMapIn = new ObjectInputStream(fileIn);
            nodeMap = (NodeMap)nodeMapIn.readObject();
            nodeMapIn.close();
            fileIn.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    private static void saveNodeMap(){
        try{
                FileOutputStream fileOut = new FileOutputStream(NODEMAP);
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
    
    public static void printLastValues(){
        System.out.println("Most recent values:");
        for(Map.Entry<Integer,Node> nodeEntry: nodeMap.getNodeMap().entrySet()){
            Node node = nodeEntry.getValue();
            for(Map.Entry<String, SensorValue> valueEntry: node.getLastValues().entrySet()){
                SensorValue value = valueEntry.getValue();
                System.out.println(node.getName() + " : " + value.getTime() + " : " 
                        + value.getType() + " : " + value.getValue());
            }
        }
    }
}
