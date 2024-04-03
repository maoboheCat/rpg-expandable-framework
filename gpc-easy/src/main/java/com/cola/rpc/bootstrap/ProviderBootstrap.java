package com.cola.rpc.bootstrap;

import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RegistryConfig;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.exception.ErrorCode;
import com.cola.rpc.exception.RpcException;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.model.ServiceRegisterInfo;
import com.cola.rpc.registry.LocalRegistry;
import com.cola.rpc.registry.Registry;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpc.server.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 服务提供者初始化
 * @author Maobohe
 * @createData 2024/4/2 15:18
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RpcException(ErrorCode.REGISTRY_REGISTER_ERROR);
            }
        }
        // 启动服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
