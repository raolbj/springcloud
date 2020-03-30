package com.rao.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HelloController {
    
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);
    
    @Autowired
    private DiscoveryClient client;

    @Qualifier("eurekaRegistration")
    @Autowired
    private Registration registration;

    @Resource
    private KafkaTemplate<String,Object> kafkaTemplate;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public  String index(){
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        for (ServiceInstance serviceInstance:instances){
            logger.info("/hello,host:"+ serviceInstance.getHost() + " service_id:" + serviceInstance.getServiceId());
        }

        return "Hello World";
    }

    @GetMapping("/message/send")
    public boolean send(@RequestParam("test") String message){
        kafkaTemplate.send("test1",message);
        return true;
    }
}
