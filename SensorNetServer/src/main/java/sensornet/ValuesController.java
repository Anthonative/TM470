/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensornet;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author antho_000
 */
@RestController
public class ValuesController {
    @RequestMapping(value="/nodevaluehistory", method=RequestMethod.GET, produces="application/json")
     public Map<String, Object[]> nodeValueHistory(@RequestParam(value="nodeID", required=true) int nodeID){
         ConcurrentSkipListMap<String, ConcurrentSkipListMap<LocalDateTime, SensorValue>> valuesHistory = 
                 Application.getNodeMap().getNode(nodeID).getValueHistory();
         Map<String, Object[]> out = new TreeMap();
         for(Map.Entry entry : valuesHistory.entrySet()){
             ConcurrentSkipListMap<LocalDateTime, SensorValue> valuesMap = (ConcurrentSkipListMap)entry.getValue();
             Object[] valuesArray = valuesMap.entrySet().toArray();
             for(int i = 0; i < valuesMap.size(); i++)
             out.put((String)entry.getKey(), valuesArray);
         }
         return out;
     }
}
