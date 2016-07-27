/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipctest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 *
 * @author antho_000
 */
public class IPCTest {
    /**
     * @param args the command line arguments
     */
    private static final String INFIFO = "/etc/sensornet/sensorvalues";
    public static void main(String[] args) {
        // TODO code application logic here
        BufferedReader in;
        System.out.println("Opening inFIFO");
        try{
            in = new BufferedReader(new FileReader(INFIFO));
            System.out.println("Open");
            
            while(true){
                if(in.ready()){
                    printValues(in);
                }
            }
        }
        catch(IOException e){
            System.out.println("ERROR: Could not open input FIFO" + e);
            System.exit(-1);
        }
        
    }
    
    private static void printValues(BufferedReader in){
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
}
