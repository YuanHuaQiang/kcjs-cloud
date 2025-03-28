package com.kcjs.cloud.provider.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * Java 标准库中的接口，继承自 Executor
     * @return
     */
    @Bean(name = "seckillExecutor")
    public ExecutorService seckillExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maximumPoolSize = corePoolSize * 2;
        long keepAliveTime = 60L;
        /**
         *
         */

        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(), //使用 SynchronousQueue，避免任务堆积，直接交由线程处理。
                new NamedThreadFactory("seckill-executor-"),
                new ThreadPoolExecutor.CallerRunsPolicy() // 采用调用者运行策略进行限流
        );
    }

    /**
     *  Spring 提供的线程池实现，基于 ExecutorService 封装，适用于异步任务、批处理任务和 Web 应用后台任务。
     * @return
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("task-exec-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    // 自定义线程工厂，便于线程命名和监控
    private static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private int count = 1;

        NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(prefix + count++);
            thread.setDaemon(false);
            thread.setUncaughtExceptionHandler((t, e) -> log.error("线程 {} 异常: {}", t.getName(), e.getMessage()));
            return thread;
        }
    }
}