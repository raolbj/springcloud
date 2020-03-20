package com.rao.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {
    
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);
    
    @Autowired
    private DiscoveryClient client;

    @Qualifier("eurekaRegistration")
    @Autowired
    private Registration registration;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public  String index(){
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        for (ServiceInstance serviceInstance:instances){
            logger.info("/hello,host:"+ serviceInstance.getHost() + " service_id:" + serviceInstance.getServiceId());
        }

        return "Hello World";
    }
}
