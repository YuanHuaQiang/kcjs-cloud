package com.kcjs.cloud.utils;

import java.util.concurrent.atomic.AtomicReference;

public class HybridSpinLock {
    private final AtomicReference<Thread> owner = new AtomicReference<>();

    // 最大自旋次数，转几下试试
    private static final int MAX_SPIN = 100;

    public void lock() {
        Thread current = Thread.currentThread();
        int spinCount = 0;

        // Step 1: 尝试自旋抢锁
        while (!owner.compareAndSet(null, current)) {
            if (++spinCount > MAX_SPIN) {
                // Step 2: 自旋失败，进入降级方案
                synchronized (this) {
                    while (owner.get() != null) {
                        try {
                            this.wait(); // 阻塞等待锁释放
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    owner.set(current); // 拿到锁
                }
                return;
            }
            Thread.yield(); // 让一下 CPU
        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();
        if (owner.compareAndSet(current, null)) {
            synchronized (this) {
                this.notify(); // 唤醒可能在等锁的线程
            }
        }
    }
}