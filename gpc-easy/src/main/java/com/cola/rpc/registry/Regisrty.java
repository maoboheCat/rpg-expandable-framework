package com.cola.rpc.registry;

import com.cola.rpc.config.RegistryConfig;
import com.cola.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心
 * @author Maobohe
 * @createData 2024/3/23 11:30
 */
public interface Regisrty {

    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务注册（服务端）
     * @param serviceMateInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMateInfo) throws Exception;

    /**
     * 服务注销（服务端）
     * @param serviceMateInfo
     */
    void unRegister(ServiceMetaInfo serviceMateInfo);

    /**
     * 服务发现（获取某服务的所有节点，消费端）
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();
}
