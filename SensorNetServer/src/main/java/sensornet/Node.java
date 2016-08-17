/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author antho_000
 */
public class Node implements Serializable{
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
    private volatile ConcurrentSkipListMap<String, ConcurrentSkipListMap<LocalDateTime, SensorValue>> valueHistory;
    /**
     * @serial 
     */
    private volatile ConcurrentSkipListMap<String, SensorValue> lastValues;
        
     Node(int nodeID){
        this.nodeID = nodeID;
        this.name = String.valueOf(nodeID);
        this.lastValues = new ConcurrentSkipListMap();
        this.valueHistory = new ConcurrentSkipListMap();
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
        SensorValue newValue = new SensorValue(time,type,value);
        if(getLastValues().containsKey(type)){
            if(!valueHistory.containsKey(type)){
                getValueHistory().put(type, new ConcurrentSkipListMap());
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
    public synchronized ConcurrentSkipListMap<String, ConcurrentSkipListMap<LocalDateTime, SensorValue>> getValueHistory() {
        return valueHistory;
    }
    
    public synchronized ConcurrentSkipListMap<String, Map<LocalDateTime, SensorValue>> getValueHistoryDescending() {
        ConcurrentSkipListMap outMap = new ConcurrentSkipListMap<>();
        for(Entry entry : this.getValueHistory().entrySet()){
            ConcurrentSkipListMap valuesMap = (ConcurrentSkipListMap)entry.getValue();
            synchronized(valuesMap){
                outMap.put(entry.getKey(), valuesMap.descendingMap());
            }
        }
        return outMap;
    }

    /**
     * @return the lastValues
     */
    public synchronized ConcurrentSkipListMap<String, SensorValue> getLastValues() {
        return lastValues;
    }
}
