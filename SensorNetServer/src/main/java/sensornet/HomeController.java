package sensornet;
 
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
        
        @RequestMapping("/node")
        public String node(@RequestParam(value="nodeID", required=true) int nodeID, Model model){
            Node node = Application.getNodeMap().getNode(nodeID);
            model.addAttribute("node", node);
            ConcurrentSkipListMap<String, ConcurrentSkipListMap<LocalDateTime, SensorValue>> valueHistory =
                    Application.getNodeMap().getNode(nodeID).getValueHistory();
            ConcurrentSkipListMap<String, ConcurrentSkipListMap<LocalDateTime, SensorValue>> valueHistoryShort =
                    new ConcurrentSkipListMap();
            for(Map.Entry<String, ConcurrentSkipListMap<LocalDateTime, SensorValue>> type : valueHistory.entrySet()){
                if(type.getValue().size() < 1000){
                    valueHistoryShort.put(type.getKey(), type.getValue());
                }
                else{
                    ConcurrentSkipListMap<LocalDateTime, SensorValue> shortValues = 
                            new ConcurrentSkipListMap(
                                    (Comparator<LocalDateTime> & Serializable)
                                            (LocalDateTime a, LocalDateTime b) -> -a.compareTo(b));
                    int count = 0;
                    for(Map.Entry<LocalDateTime, SensorValue> value : type.getValue().entrySet()){
                        if (count > 999) break;
                        shortValues.put(value.getKey(), value.getValue());
                        count++;
                    }
                    valueHistoryShort.put(type.getKey(), shortValues);
                }
            }
            model.addAttribute("valueHistory", valueHistoryShort);
            return "node";
        }
        
        @RequestMapping("/nodesettings")
        public String nodeSettings(@RequestParam(value="nodeID", required=true) int nodeID, Model model){
            Node node = Application.getNodeMap().getNode(nodeID);
            model.addAttribute("node", node);
            return "nodesettings"; 
        }
        
        @RequestMapping(value="/setnodename", method=RequestMethod.POST)
        public String setNodeName(@RequestParam(value="nodeID", required=true) int nodeID,
                @RequestParam(value="name", required=true) String name, Model model){
            Node node = Application.getNodeMap().getNode(nodeID);
            node.setName(name);
            Application.getNodeMap().save();
            model.addAttribute("node", node);    
            return "nodesettings";
        }
        
        
        
        @RequestMapping("/setupdatefrequency")
        public String setNodeUpdateFrequency(@RequestParam(value="nodeID", required=true) int nodeID, 
                @RequestParam(value="frequency", required=true) int frequency, Model model){
            String instruction = nodeID + ";U;" + frequency;
            
            Application.getNodeMap().addInstruction(instruction);
            System.out.println(frequency);
            model.addAttribute(Application.getNodeMap().getNode(nodeID));
            return "nodesettings";
        }
        
        
        @ModelAttribute("lastvaluesall")
        public Map<String, Map<String,SensorValue>> lastVaulesAll(){
            Map<Integer, Node> nodeMap = Application.getNodeMap().getNodeMap();
            TreeMap<String, Map<String,SensorValue>> values;
            values = new TreeMap();
            for(Node node : nodeMap.values()){
                values.put(node.getName(), node.getLastValues());
            }
        return values;
        }
        
        @ModelAttribute("nodelist")
        public Collection<Node>  getNodes(){
            return Application.getNodeMap().getNodeMap().values();
        }
        
        
}