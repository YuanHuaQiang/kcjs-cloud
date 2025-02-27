package com.kcjs.cloud.gateway.controller;


import com.kcjs.cloud.gateway.feign.HelloServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloServiceFeign helloService;

    public HelloController(HelloServiceFeign helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name){
        return helloService.hello(name);
    }
}
