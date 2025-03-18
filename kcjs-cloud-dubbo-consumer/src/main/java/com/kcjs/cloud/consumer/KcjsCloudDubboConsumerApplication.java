package com.kcjs.cloud.consumer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableDubbo
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kcjs"})
public class KcjsCloudDubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KcjsCloudDubboConsumerApplication.class, args);
    }

}
