package com.cola.rpc.loadbalancer.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 循环轮询负载均器缓存
 * @author Maobohe
 * @createData 2025/9/12 19:03
 */
public class RoundRobinStateCache {

    private final ConcurrentHashMap<String, AtomicInteger> serviceIndexCache = new ConcurrentHashMap<>();

    /**
     * 获取下一个轮询索引
     * @param serviceKey
     * @param instanceCount
     * @return
     */
    public int getNextIndex(String serviceKey, int instanceCount) {
        // 获取或创建该服务的轮询索引
        AtomicInteger currentIndex = serviceIndexCache.computeIfAbsent(
                serviceKey, k -> new AtomicInteger(0)
        );
        // 获取并增加索引
        return getAndIncrement(currentIndex, instanceCount);
    }

    /**
     * 获取并增加索引
     * @param atomicInteger
     * @param instanceCount
     * @return
     */
    private int getAndIncrement(AtomicInteger atomicInteger, int instanceCount) {
        while (true) {
            int current = atomicInteger.get();
            int next = (current + 1) % instanceCount;
            if (atomicInteger.compareAndSet(current, next)) {
                return current;
            }
            // CAS 失败则重试
        }
    }

    public void removeServiceState(String serviceKey) {
        serviceIndexCache.remove(serviceKey);
    }

    public void clearAll() {
        serviceIndexCache.clear();
    }


}
