package com.cola.rpc.loadbalancer.impl;

import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 加权随机法
 * @author Maobohe
 * @createData 2024/3/31 11:45
 */
public class WeightRandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

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
        // 计算服务器权重总和
        if (totalWeight == 0) {
            for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
                totalWeight += serviceMetaInfo.getWeight();
            }
        }
        // 生成一个随机数
        int randomWeight = random.nextInt(totalWeight) + 1;
        // 遍历服务器列表，根据服务器权重选择对应地址
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            int weight = serviceMetaInfo.getWeight();
            if (randomWeight <= weight) {
                return serviceMetaInfo;
            }
            randomWeight -= weight;
        }
        return null;
    }
}
