/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornetsimple;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeMap;

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
    private final TreeMap<String, TreeMap<LocalDateTime, SensorValue>> valueHistory;
    /**
     * @serial 
     */
    private final TreeMap<String, SensorValue> lastValues;
        
     Node(int nodeID){
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
     
    public void addValue(LocalDateTime time, String inValue) throws Exception{
        SensorValue newValue;
        String[] split = inValue.split(";");
        if (split.length != 3){
            Exception e = new Exception("Invalid sensor value string from mesh");
            throw e;
        }
        String type = split[1];
        double value = Double.parseDouble(split[2]);
        newValue = new SensorValue(time,type,value);
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
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the valueHistory
     */
    public TreeMap<String, TreeMap<LocalDateTime, SensorValue>> getValueHistory() {
        return valueHistory;
    }

    /**
     * @return the lastValues
     */
    public TreeMap<String, SensorValue> getLastValues() {
        return lastValues;
    }
}
