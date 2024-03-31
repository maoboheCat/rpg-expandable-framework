package com.cola.rpc.loadbalancer;

/**
 * 负载均衡器键名常量
 * @author Maobohe
 * @createData 2024/3/30 17:12
 */
public interface LoadBalancerKey {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性哈希
     */
    String CONSISTENT_HASH = "consistentHash";

    /**
     * 加权随机
     */
    String WEIGHT_RANDOM = "weightRandom";

    /**
     * 加权轮询
     */
    String WEIGHT_ROUND_ROBIN = "weightRoundRobin";

}
