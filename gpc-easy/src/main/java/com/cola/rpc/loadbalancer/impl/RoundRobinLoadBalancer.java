package com.cola.rpc.loadbalancer.impl;

import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.loadbalancer.cache.RoundRobinStateCache;
import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 循环轮询负载均器
 * @author Maobohe
 * @createData 2024/3/30 16:16
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final RoundRobinStateCache stateCache = new RoundRobinStateCache();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 只有一个服务,无需轮询
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        String serviceKey = serviceMetaInfoList.get(0).getServiceKey();
        int index = stateCache.getNextIndex(serviceKey, size);
        return serviceMetaInfoList.get(index);
    }
}
