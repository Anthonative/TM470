/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
/**
 *
 * @author antho_000
 */
public class NodeMap implements Serializable{
    /**
     * @serial 
     */
    private volatile Map<Integer, Node> nodeMap;
    private volatile List<String> instructionList;
    public NodeMap(){
        nodeMap = new TreeMap<>();
        instructionList = new ArrayList<>();
    }
    
   /**
    * @param nodeID
    * @return 
    */
    public synchronized Node getNode(int nodeID){
        return getNodeMap().get(nodeID);
    }
    
    /*
    * Returns true if a node with nodeID is stored in the node map.
    */
    public synchronized boolean hasNode(int nodeID){
        return getNodeMap().containsKey(nodeID);
    }

    /*
    *Instantiates a new Node with nodeID and adds to node map
    */
    public synchronized void addNode(int nodeID){
        Node newNode = new Node(nodeID);
        getNodeMap().put(nodeID, newNode);
    }
    /**
     * @return the nodeMap
     */
    public synchronized Map<Integer, Node> getNodeMap() {
        return nodeMap;
    }
    
    public synchronized void addInstruction(String instruction){
        instructionList.add(instruction);
    }

    /**
     * @return the instructionList
     */
    public List<String> getInstructionList() {
        return instructionList;
    }
}
