package com.kcjs.cloud.sp.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kcjs"})
public class KcjsCloudSpProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KcjsCloudSpProviderApplication.class, args);
    }

}
