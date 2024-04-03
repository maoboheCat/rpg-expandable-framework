package com.cola.rpcspringbootstarter.bootstrap;

import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RegistryConfig;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.registry.LocalRegistry;
import com.cola.rpc.registry.Registry;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpcspringbootstarter.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * RPC 服务提供者启动
 * @author Maobohe
 * @createData 2024/4/2 20:45
 */
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行 -- 注册服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 需要注册服务
            // 获取服务的基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            // 默认处理
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            // 注册服务
            // 1. 本地注册
            LocalRegistry.register(serviceName, beanClass);
            // 注册服务到注册中心
            // 全局配置
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败,", e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
