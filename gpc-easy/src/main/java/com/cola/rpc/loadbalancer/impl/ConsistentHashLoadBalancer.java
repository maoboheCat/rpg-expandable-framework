package com.cola.rpc.loadbalancer.impl;

import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希均衡负载器
 * @author Maobohe
 * @createData 2024/3/30 16:43
 */
@Slf4j
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性哈希环，存放虚拟节点
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = setHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        // 获取调用的Hash值2
        int hash = getHash(requestParams);
        // 选择最接近大于等于调用请求hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 如果没有大于等于调用请求Hash 值的虚拟节点，则返回环首部的节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 哈希算法
     * @return
     */
    private int getHash(Object key) {
        boolean getHashStatus = true;
        int hash = 0;
        try {
            hash = InetAddress.getLocalHost().getHostAddress().hashCode();
        } catch (UnknownHostException e) {
            getHashStatus = false;
            log.error("ip获取失败");
        }
        if (getHashStatus) {
            return hash;
        }
        return setHash(key);
    }

    private int setHash(Object key) {
        return key.hashCode();
    }
}
