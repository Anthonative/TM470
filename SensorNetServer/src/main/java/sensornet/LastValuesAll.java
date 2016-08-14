/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Tony
 */
public class LastValuesAll
{
     private final long id;
     private final Map<String, Map<String, SensorValue>> values;
     
     public LastValuesAll(long id){
         this.id = id;
         Map<Integer, Node> nodeMap = Application.getNodeMap().getNodeMap();
         values = new TreeMap();
         for(Node node : nodeMap.values()){
             values.put(node.getName(), node.getLastValues());
         }
         
     }

    /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }

    /**
     * @return the values
     */
    public Map<String, Map<String, SensorValue>> getValues()
    {
        return values;
    }
   
    
}
