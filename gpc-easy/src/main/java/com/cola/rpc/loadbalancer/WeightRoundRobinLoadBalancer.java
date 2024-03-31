package com.cola.rpc.loadbalancer;

import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加权轮询法
 * @author Maobohe
 * @createData 2024/3/31 12:19
 */
public class WeightRoundRobinLoadBalancer implements LoadBalancer{

    private final AtomicInteger currentIndex = new AtomicInteger();

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
        // 获取调用服务器
        while (true) {
            int index = currentIndex.getAndIncrement() % size;
            int weight = serviceMetaInfoList.get(index).getWeight();
            if (weight >= totalWeight) {
                return serviceMetaInfoList.get(index);
            }
            totalWeight -= weight;
        }
    }
}
