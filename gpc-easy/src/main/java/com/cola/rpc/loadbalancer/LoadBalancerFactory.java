package com.cola.rpc.loadbalancer;

import com.cola.rpc.loadbalancer.constant.LoadBalancerKey;
import com.cola.rpc.spi.SpiLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * 负载均衡器工厂（工厂模式，用于负载均衡器对象）
 * @author Maobohe
 * @createData 2024/3/31 9:28
 */
public class LoadBalancerFactory {

    /**
     * 默认负载均衡器
     */
    private static final String DEFAULT_LOAD_BALANCER = LoadBalancerKey.ROUND_ROBIN;

    public static LoadBalancer getInstance(String key) {
        if (StringUtils.isBlank(key)) {
            key = DEFAULT_LOAD_BALANCER;
        }
        key = StringUtils.lowerCase(key);
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
