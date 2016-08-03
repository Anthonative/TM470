package sensornet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication
public class Application {
    public static final String INFIFO_PATH = "/etc/sensornet/sensorvalues";
    public static final String NODE_MAP_PATH = "/etc/sensornet/nodemap.ser";
    private static volatile NodeMap nodeMap;
    private static SensorNetThread sensorNetThread;
    
     @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }        

    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        nodeMap = initNodeMap();
        sensorNetThread  = new SensorNetThread(getNodeMap());
        sensorNetThread.run();
    }
    
    
    private static NodeMap initNodeMap(){
        File nodeMapFile = new File(NODE_MAP_PATH);
        NodeMap nMap;
        if(!nodeMapFile.isFile()){
            try{
            nodeMapFile.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
            nMap = new NodeMap();
        }
        else{
            try{
                FileInputStream fileIn = new FileInputStream(NODE_MAP_PATH);
                ObjectInputStream nodeMapIn = new ObjectInputStream(fileIn);
                nMap = (NodeMap)nodeMapIn.readObject();
                nodeMapIn.close();
                fileIn.close();
            }
            catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
                return null;
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
                System.exit(-1);
                return null;
            }
        }
        return nMap;
    }

    /**
     * @return the nodeMap
     */
    public static NodeMap getNodeMap()
    {
        return nodeMap;
    }

    /**
     * @return the nodeMap
     */
    
   
  
   
}