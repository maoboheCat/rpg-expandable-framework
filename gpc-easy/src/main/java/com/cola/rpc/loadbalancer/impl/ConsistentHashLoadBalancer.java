package com.cola.rpc.loadbalancer.impl;

import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    private final ConcurrentHashMap<Integer, ServiceMetaInfo> virtualNodes = new ConcurrentHashMap<>();

    /**
     * 当前服务的哈希环版本号，用于判断是否需要更新
     */
    private String currentServiceHash = "";

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 判断服务节点是否变化，构建虚拟节点环
        String newServiceHash = generateServiceHash(serviceMetaInfoList);
        if (!newServiceHash.equals(currentServiceHash)) {
            for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
                for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                    String virtualNodeKey = serviceMetaInfo.getServiceAddress() + "#" + i;
                    int hash = getHash(virtualNodeKey);
                    virtualNodes.put(hash, serviceMetaInfo);
                }
            }
            currentServiceHash = newServiceHash;
        }
        // 选择最接近大于等于调用请求hash 值的虚拟节点
        TreeMap<Integer, ServiceMetaInfo> virtualNodesTemp = new TreeMap<>(virtualNodes);
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodesTemp.ceilingEntry(getHash(requestParams));
        if (entry == null) {
            // 如果没有大于等于调用请求Hash值的虚拟节点，则返回环首部的节点
            entry = virtualNodesTemp.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 生成服务列表的哈希标识，用于判断是否需要重新构建环
     * @param serviceMetaInfoList
     * @return
     */
    private String generateServiceHash(List<ServiceMetaInfo> serviceMetaInfoList) {
        List<String> addresses = serviceMetaInfoList.stream()
                .map(ServiceMetaInfo::getServiceAddress)
                .sorted()
                .collect(Collectors.toList());
        return String.join(",", addresses);
    }

    /**
     * 哈希算法（可实现）
     * @param param
     * @return
     */
    private int getHash(Object param) {
        // 使用简单的哈希算法，足够用于一致性哈希
        return param.hashCode();
    }
}
