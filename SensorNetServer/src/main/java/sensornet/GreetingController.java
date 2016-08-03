package sensornet;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, stringLastValues()));
    }
    
    @RequestMapping("/a")
    String index() {
        return "index";
    }
    private String stringLastValues(){
        String out = "Most recent values:\n";
        for (Map.Entry<Integer,NodeSerializable> nodeEntry : Application.getNodeMap().getNodeMap().entrySet()) {
            NodeSerializable node = nodeEntry.getValue();
            for(Map.Entry<String, SensorValueSerializable> valueEntry: node.getLastValues().entrySet()){
                SensorValueSerializable value = valueEntry.getValue();
                out = out + node.getName() + " : " + value.getTime() + " : " 
                        + value.getType() + " : " + value.getValue() + "\n";
            }
        }
        return out;
    }
}