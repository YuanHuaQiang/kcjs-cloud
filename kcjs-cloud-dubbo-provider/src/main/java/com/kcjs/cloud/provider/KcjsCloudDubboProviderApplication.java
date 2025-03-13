package com.kcjs.cloud.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
@EnableRetry
@MapperScan("com.kcjs.**.mapper")
@ComponentScan(basePackages = {"com.kcjs"})
public class KcjsCloudDubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KcjsCloudDubboProviderApplication.class, args);
    }

}
