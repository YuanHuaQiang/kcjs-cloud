package com.kcjs.cloud.consumer.controller.hello;


import com.kcjs.cloud.api.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @DubboReference
    private HelloService helloService;

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name){
        return helloService.hello(name);
    }
}
