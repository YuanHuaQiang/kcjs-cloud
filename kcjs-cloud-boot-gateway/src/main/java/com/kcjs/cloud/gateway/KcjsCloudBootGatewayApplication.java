package com.kcjs.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class KcjsCloudBootGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KcjsCloudBootGatewayApplication.class, args);
    }

}
