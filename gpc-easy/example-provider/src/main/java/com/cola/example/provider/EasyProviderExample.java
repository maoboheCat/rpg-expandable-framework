package com.cola.example.provider;

import com.cola.example.common.service.UserService;
import com.cola.rpc.registry.LocalRegistry;
import com.cola.rpc.server.VertxHttpServer;

/**
 * 简易服务提供者
 * @author Maobohe
 * @createData 2024/3/17 21:19
 */
public class EasyProviderExample {

    public static void main (String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 启用web服务
        VertxHttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
