package com.cola.example.consumer;

import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.utils.ConfigUtils;

/**
 * @author Maobohe
 * @createData 2024/3/19 20:05
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
