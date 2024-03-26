package com.cola.rpc;

import com.cola.rpc.config.RegistryConfig;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.constant.RpcConstant;
import com.cola.rpc.registry.Registry;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * RPC 框架应用
 * 使用单例模式确保在框架项目启动时从配置文件中读取配置并创建对象实例
 * @author Maobohe
 * @createData 2024/3/19 16:52
 */
@Slf4j
public class RpcApplication {
    public static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化,支持传入自定义配置
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("------> rpc init, config = {}", newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry regisrty = RegistryFactory.getInstance(registryConfig.getRegistry());
        regisrty.init(registryConfig);
        log.info("------> registry init, config = {}", registryConfig);
        // 创建并注册Shutdown Hook，JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(regisrty::destroy));
    }

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 加载失败使用默认配置
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
