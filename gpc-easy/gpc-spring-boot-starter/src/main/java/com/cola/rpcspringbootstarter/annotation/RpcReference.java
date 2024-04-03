package com.cola.rpcspringbootstarter.annotation;

import com.cola.rpc.constant.RpcConstant;
import com.cola.rpc.fault.retry.RetryStrategyKey;
import com.cola.rpc.fault.tolerant.TolerantStrategyJKey;
import com.cola.rpc.loadbalancer.LoadBalancerKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解（用于注入服务）
 * @author Maobohe
 * @createData 2024/4/2 19:53
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务版本
     */
    String serverVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     */
    String loadBalancer() default LoadBalancerKey.ROUND_ROBIN;

    /**
     * 重试策略
     */
    String RetryStrategy() default RetryStrategyKey.NO;

    /**
     * 容错策略
     */
    String tolerantStrategy() default TolerantStrategyJKey.FAIL_FAST;

    /**
     * 模拟调用
     */
    boolean mock() default false;
}
