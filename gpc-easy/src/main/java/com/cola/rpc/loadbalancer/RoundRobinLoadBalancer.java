package com.cola.rpc.loadbalancer;

import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 循环轮询负载均器
 * @author Maobohe
 * @createData 2024/3/30 16:16
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    private final AtomicInteger currentIndex = new AtomicInteger();

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
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
