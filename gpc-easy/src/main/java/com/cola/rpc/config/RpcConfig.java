package com.cola.rpc.config;

import com.cola.rpc.fault.retry.constant.RetryStrategyKey;
import com.cola.rpc.fault.tolerant.constant.TolerantStrategyKey;
import com.cola.rpc.loadbalancer.constant.LoadBalancerKey;
import com.cola.rpc.serializer.constant.SerializerKey;
import lombok.Data;

/**
 * RPC 配置文件
 * 设置一些默认配置
 * @author Maobohe
 * @createData 2024/3/19 16:06
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "rpc-easy";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKey.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKey.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKey.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKey.FAIL_BACK;
}
