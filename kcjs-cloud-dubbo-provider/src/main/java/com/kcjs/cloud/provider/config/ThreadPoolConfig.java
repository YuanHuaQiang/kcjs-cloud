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

        int queueCapacity = 200;
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new NamedThreadFactory("seckill-executor-"),
                new ThreadPoolExecutor.DiscardPolicy() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.warn("秒杀线程池任务被拒绝，当前队列大小: {}, 活跃线程数: {}, 总线程数: {}", 
                                executor.getQueue().size(), executor.getActiveCount(), executor.getPoolSize());
                    }
                }
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

    public static void main(String[] args) {
        // 定义任务逻辑
        Callable<String> task = () -> {
            // 模拟任务执行
            Thread.sleep(2100);
            return "Task Completed";
        };

        // 创建线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            // 提交任务并获取 Future 对象
            Future<String> future = executorService.submit(task);

            // 获取任务结果，设置超时时间为 2 秒
            String result = future.get(2, TimeUnit.SECONDS);
            System.out.println(result);

        } catch (InterruptedException e) {
            System.err.println("线程被中断: " + e.getMessage());
        } catch (ExecutionException e) {
            System.err.println("任务执行失败: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            System.err.println("任务超时: " + e.getMessage());
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }

        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> System.out.println("f1"));
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> System.out.println("f2"));
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> System.out.println("f3"));

        CompletableFuture.allOf(f1, f2, f3).join(); // 等待全部完成
    }



}