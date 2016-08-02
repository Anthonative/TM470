/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

/**
 *
 * @author antho_000
 */
public class LocalIO {
    private static final String INFIFO = "/etc/sensornet/sensornetout";
    private BufferedReader in;
    
    public LocalIO(){
        try{
            in = new BufferedReader(new FileReader(INFIFO));
        }
        catch(IOException e){
            System.out.println("ERROR: Could not open input FIFO" + e);
            System.exit(-1);
        }
    }
    
    public String get(){
        try{
            while(in.ready()){
            
            }
        }
        catch(IOException e){
            
        }
        return "foo";
    }
    
}
