package com.kcjs.cloud.provider.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class WindowLock {
	
    
    private AtomicLong waitingCounter = new AtomicLong(0); // 等待线程计数器
    private int maxWaiters = 100; // 最大承载能力
    
    // 动态计算窗口时间
    private long calculateWindow() {
        long currentWaiters = waitingCounter.get();
        float contentionFactor = Math.min(currentWaiters / (float)maxWaiters, 1.0f);
        
        long baseWindow = 10; // 基础窗口时间
        long dynamicWindow = (long)(baseWindow * (1 + contentionFactor));
        
        return dynamicWindow * (100 + ThreadLocalRandom.current().nextInt(0, 30)) / 100;
    }
    
    public boolean tryLock() throws InterruptedException {
        waitingCounter.incrementAndGet(); // 进入等待
        try {
            while (!acquireLock()) { // 尝试获取锁，如果没有获取到锁就进入到循环里面
                long window = calculateWindow(); // 计算窗口时间
                Thread.sleep(window);//	开始进入休眠状态，休眠后线程进入就绪状态
            }
            return true;
        } finally {
            waitingCounter.decrementAndGet(); // 线程成功获取到锁，退出等待
        }
    }
    
    private AtomicBoolean lock = new AtomicBoolean(false);
    
    //	没有加锁、也没有加volatile，线程不安全的
    private  static volatile int a = 0;
    
    public static void main(String[] args) {
    	int thread = 500;
    	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 500, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));
    	CountDownLatch countDownLatch = new CountDownLatch(thread);
    	WindowLock windowLock = new WindowLock();
    	for (int i = 0; i < thread; i++) {
    		threadPoolExecutor.execute(() -> {
    			try {
					windowLock.tryLock();//	获取锁
					a++;
                    System.out.println(a);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					windowLock.unLock(); //	释放锁
					countDownLatch.countDown();
				}
    			
    			//				这是线程不安全的代码块，你可以解开代码注释上面测试下
//    			***************************************************************
    			/*a++;
                System.out.println(a);
    			countDownLatch.countDown();*/
    		});
		}
    	
    	threadPoolExecutor.shutdown();

    	
    	
    	//	等待所有线程跑完
    	try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println(a);
	}

    
    private boolean acquireLock() throws InterruptedException {
        return lock.compareAndSet(false, true);
    }
    
    private void unLock() {
    	lock.compareAndSet(true, false);
    }
    
}
