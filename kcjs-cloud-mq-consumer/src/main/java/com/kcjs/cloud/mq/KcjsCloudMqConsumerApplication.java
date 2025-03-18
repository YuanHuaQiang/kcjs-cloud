package com.kcjs.cloud.mq;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableDubbo
@EnableDiscoveryClient
@EnableRetry
@ComponentScan(basePackages = {"com.kcjs"})
public class KcjsCloudMqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KcjsCloudMqConsumerApplication.class, args);
    }

}
