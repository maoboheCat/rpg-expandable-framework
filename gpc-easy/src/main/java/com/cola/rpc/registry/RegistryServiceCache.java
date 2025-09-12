package com.cola.rpc.registry;

import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心本地缓存
 * @author Maobohe
 * @createData 2024/3/24 16:23
 */
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
     Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写缓存
     * @param serviceKey
     * @param newServiceCache
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * 读缓存
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        return this.serviceCache.get(serviceKey);
    }

    /**
     * 清空缓存
     * @param serviceKey
     */
    void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }

}
