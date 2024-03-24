package com.cola.example.provider;

import com.cola.example.common.service.UserService;
import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RegistryConfig;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.registry.LocalRegistry;
import com.cola.rpc.registry.Regisrty;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpc.server.HttpServer;
import com.cola.rpc.server.VertxHttpServer;

/**
 * @author Maobohe
 * @createData 2024/3/24 9:59
 */
public class ProvideExample {

    public static void main(String[] args) {
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Regisrty regisrty = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            regisrty.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(rpcConfig.getServerPort());
    }
}
