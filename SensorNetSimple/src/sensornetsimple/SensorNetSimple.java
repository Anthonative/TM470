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
                    readValues(in);
                }
            }
        }
        catch(IOException e){
            System.out.println("ERROR: Could not open input FIFO" + e);
            System.exit(-1);
        }
        
    }
    
    private static void readValues(BufferedReader in){
        try{
            while(in.ready()){
              System.out.println("Reading next value");
              System.out.println(LocalDateTime.now() + "," + in.readLine());
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
}
