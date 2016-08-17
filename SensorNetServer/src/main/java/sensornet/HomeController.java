package sensornet;
 
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
        
        @RequestMapping("/node")
        public String node(@RequestParam(value="nodeID", required=true) int nodeID, Model model){
            model.addAttribute("node", Application.getNodeMap().getNode(nodeID));
            return "node";
        }
        
        @RequestMapping("/nodesettings")
        public String nodeSettings(@RequestParam(value="nodeID", required=true) int nodeID, Model model){
            model.addAttribute("node", Application.getNodeMap().getNode(nodeID));
            return "nodesettings"; 
        }
        
        @RequestMapping("/setupdatefrequency")
        public void setNodeUpdateFrequency(@RequestParam(value="nodeID", required=true) int nodeID, 
                @RequestParam(value="frequency", required=true) int frequency, Model model){
            String instruction = nodeID + ";U;" + frequency;
            Application.getNodeMap().addInstruction(instruction);
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