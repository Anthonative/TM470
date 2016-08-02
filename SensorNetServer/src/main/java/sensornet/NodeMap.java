/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.util.Map;
import java.io.Serializable;
import java.util.TreeMap;
/**
 *
 * @author antho_000
 */
public class NodeMap implements Serializable{
    /**
     * @serial 
     */
    private final Map<Integer, Node> nodeMap;
    
    public NodeMap(){
        nodeMap = new TreeMap<>();
    }
    
   /**
    * Returns Node the object which has nodeID. If nodeID is unknown, creates new node
    * and adds to node map.
    * @param nodeID
    * @return 
    */
    public Node getNode(int nodeID){
        if(getNodeMap().containsKey(nodeID)){
            return getNodeMap().get(nodeID);
        }
        else{
            Node newNode = new Node(nodeID);
            getNodeMap().put(nodeID, newNode);
            return getNodeMap().get(nodeID);
        }
           
    }

    /**
     * @return the nodeMap
     */
    public Map<Integer, Node> getNodeMap() {
        return nodeMap;
    }
}
