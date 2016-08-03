package sensornet;
 
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
        
        @ModelAttribute("lastvaluesall")
        public Map lastVaulesAll(){
            Map<Integer, NodeSerializable> nodeMap = Application.getNodeMap().getNodeMap();
            TreeMap<String, Map<String,SensorValueSerializable>> values;
            values = new TreeMap();
            for(NodeSerializable node : nodeMap.values()){
                values.put(node.getName(), node.getLastValues());
            }
        return values;
    }
}