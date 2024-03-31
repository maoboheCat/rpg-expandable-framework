package com.cola.rpc.loadbalancer;

import com.cola.rpc.spi.SpiLoader;

/**
 * 负载均衡器工厂（工厂模式，用于负载均衡器对象）
 * @author Maobohe
 * @createData 2024/3/31 9:28
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
