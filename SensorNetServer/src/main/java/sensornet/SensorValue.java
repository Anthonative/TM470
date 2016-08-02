/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author antho_000
 */
public class SensorValue implements Serializable{
    /**
     * @serial 
     */
    private final LocalDateTime time;
    /**
     * @serial 
     */
    private final String type;
    /**
     * @serial 
     */
    private final double value;

    SensorValue(LocalDateTime time, String type, double value){
        this.time=time;
        this.type=type;
        this.value=value;
    }
    /**
     * @return the time
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
    
}
