package com.kcjs.cloud.gateway.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.RedisConnection;
import org.redisson.misc.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() throws IOException {

        // 👇 从 YAML 配置加载
        Config config = Config.fromYAML(
                this.getClass().getClassLoader().getResource("redisson-master-slave.yaml")
        );

        // 👇 设置 nat 映射（容器内 IP 映射宿主机）
        config.useSentinelServers()
                .setNatMapper(addr -> {
                    String ip = addr.getHost();
                    if (ip.startsWith("172.")) {
                        return new RedisURI("redis","192.168.31.152", addr.getPort()); //暂时无法解决，直接使用宿主机IP
                    }
                    return addr;
                });

        return Redisson.create(config);
    }
}