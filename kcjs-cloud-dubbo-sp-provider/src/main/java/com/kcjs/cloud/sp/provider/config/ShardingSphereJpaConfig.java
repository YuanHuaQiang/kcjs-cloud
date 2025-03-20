package com.kcjs.cloud.sp.provider.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.kcjs.cloud.mysql.repository")
@EntityScan(basePackages = "com.kcjs.cloud.mysql.pojo")
public class ShardingSphereJpaConfig {

}