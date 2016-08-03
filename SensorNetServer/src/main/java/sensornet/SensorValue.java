/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.time.LocalDateTime;

/**
 *
 * @author Tony
 */
public class SensorValue
{
    private final LocalDateTime time;
    private final String type;
    private final double value;
    
    public SensorValue(SensorValueSerializable sensorValue){
        this.time = sensorValue.getTime();
        this.type = sensorValue.getType();
        this.value = sensorValue.getValue();
    }

    /**
     * @return the time
     */
    public LocalDateTime getTime()
    {
        return time;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @return the value
     */
    public double getValue()
    {
        return value;
    }
}
