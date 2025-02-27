package com.kcjs.cloud.provider.service;

import com.kcjs.cloud.api.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return Thread.currentThread().getName()+"你好 " + name;
    }
}