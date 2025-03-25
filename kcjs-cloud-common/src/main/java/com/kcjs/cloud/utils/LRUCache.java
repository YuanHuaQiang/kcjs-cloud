package com.kcjs.cloud.utils;

import java.util.*;

public class LRUCache<T> {
    private final int capacity; // 缓存最大容量
    private final Deque<T> deque; // 用作 LRU 队列
    private final Set<T> cache; // 用于快速查找元素是否存在

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.deque = new ArrayDeque<>(capacity);
        this.cache = new HashSet<>();
    }

    /** 访问一个元素 */
    public void access(T item) {
        // 如果元素已存在，先删除（后续重新插入到队列头部）
        if (cache.contains(item)) {
            deque.remove(item);
        } else {
            // 如果缓存已满，移除最久未使用的元素（队列尾部）
            if (deque.size() == capacity) {
                T last = deque.removeLast();
                cache.remove(last);
            }
        }
        // 将新访问的元素放入队列头部
        deque.addFirst(item);
        cache.add(item);
    }

    /** 获取当前缓存状态 */
    public List<T> getCache() {
        return new ArrayList<>(deque);
    }

    public static void main(String[] args) {
        LRUCache<Integer> lru = new LRUCache<>(3);
        lru.access(1);
        lru.access(2);
        lru.access(3);
        System.out.println(lru.getCache()); // [3, 2, 1]

        lru.access(4); // 淘汰 1
        System.out.println(lru.getCache()); // [4, 3, 2]

        lru.access(3); // 3 变为最近使用
        System.out.println(lru.getCache()); // [3, 4, 2]
    }
}