package com.cola.rpc.loadbalancer.impl;

import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.loadbalancer.cache.RoundRobinStateCache;
import com.cola.rpc.model.ServiceMetaInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加权轮询法
 * @author Maobohe
 * @createData 2024/3/31 12:19
 */
public class WeightRoundRobinLoadBalancer implements LoadBalancer {

    private final RoundRobinStateCache stateCache = new RoundRobinStateCache();

    /**
     * 服务器权重总和
     */
    private int totalWeight = 0;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // 计算权重总和
        if (totalWeight == 0) {
            for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
                totalWeight += serviceMetaInfo.getWeight();
            }
        }
        // 获取下一个轮询索引
        int index = stateCache.getNextIndex(serviceMetaInfoList.get(0).getServiceKey(), totalWeight);
        // 获取调用服务器
        int weightSum = 0;
        for (ServiceMetaInfo service : serviceMetaInfoList) {
            weightSum += service.getWeight();
            if (index < weightSum) {
                return service;
            }
        }
        // 保底
        return serviceMetaInfoList.get(0);
    }
}
