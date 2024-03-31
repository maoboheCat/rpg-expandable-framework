package com.cola.rpc.loadbalancer;

import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（服务端适用）
 * @author Maobohe
 * @createData 2024/3/30 10:52
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
