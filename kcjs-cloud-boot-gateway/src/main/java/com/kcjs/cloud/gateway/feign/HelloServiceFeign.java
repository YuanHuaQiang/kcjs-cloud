package com.kcjs.cloud.gateway.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "kcjs-cloud-dubbo-consumer",contextId = "HelloServiceFeign")
public interface HelloServiceFeign {


    @GetMapping(value = "/hello/{name}", produces = "application/json")
    String hello(@PathVariable("name") String name);
}
