/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornetsimple;

import java.time.LocalDateTime;

/**
 *
 * @author antho_000
 */
public class Node {
    private final int nodeID;
    private String name;
        
    Node(int nodeID){
        this.nodeID = nodeID;
        this.name = String.valueOf(nodeID);
    }
    
    public void addValue(LocalDateTime time, String inValue) throws Exception{
        SensorValue newValue;
        String[] split = inValue.split(";");
        if (split.length != 3){
            Exception e = new Exception("Invalid value string");
            throw e;
        }
        String type = split[1];
        double value = Double.parseDouble(split[2]);
        newValue = new SensorValue(time,type,value);
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
}
