package com.cola.rpc.bootstrap;

import com.cola.rpc.RpcApplication;

/**
 * 服务消费者初始化
 * @author Maobohe
 * @createData 2024/4/2 15:43
 */
public class ConsumerBootstrap {

    public static void init() {
        // RPC 框架初始化
        RpcApplication.init();
    }
}
