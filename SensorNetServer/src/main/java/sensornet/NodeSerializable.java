/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 *
 * @author antho_000
 */
public class NodeSerializable implements Serializable{
    /**
     * @serial 
     */
    private final int nodeID;
    /**
     * @serial 
     */
    private String name;
    /**
     * @serial 
     */
    private volatile TreeMap<String, TreeMap<LocalDateTime, SensorValueSerializable>> valueHistory;
    /**
     * @serial 
     */
    private volatile TreeMap<String, SensorValueSerializable> lastValues;
        
     NodeSerializable(int nodeID){
        this.nodeID = nodeID;
        this.name = String.valueOf(nodeID);
        this.lastValues = new TreeMap();
        this.valueHistory = new TreeMap();
    }
    /**
     * Sets last value to input value. Moves old last value to value history
     * @param time
     * @param inValue
     * @throws Exception 
     */
     
    /**
     * Sets last value to input value.Moves old last value to value history
     * @param time
     * @param type
     * @param value
     * @throws Exception
     */
    public synchronized void addValue(LocalDateTime time, String type, double value) throws Exception{
        SensorValueSerializable newValue = new SensorValueSerializable(time,type,value);
        if(getLastValues().containsKey(type)){
            if(!valueHistory.containsKey(type)){
                getValueHistory().put(type, new TreeMap());
            }
            getValueHistory().get(type).put(time, newValue);
        }
        getLastValues().put(type, newValue);
    }
    /**
     * @return the nodeID
     */
    public int getNodeID() {
        return nodeID;
    }

    /**
     * @return the name
     */
    public synchronized String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * @return the valueHistory
     */
    public synchronized TreeMap<String, TreeMap<LocalDateTime, SensorValueSerializable>> getValueHistory() {
        return valueHistory;
    }

    /**
     * @return the lastValues
     */
    public synchronized TreeMap<String, SensorValueSerializable> getLastValues() {
        return lastValues;
    }
}
