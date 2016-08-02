package sensornet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class Application {
    private static SensorNetThread SensorNetThread;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        SensorNetThread sensornet = new SensorNetThread();
        SensorNetThread = new SensorNetThread();
    }
    
    @RequestMapping("/values")
    public String greeting() {
        return SensorNetThread.stringLastValues();
    }
}