package com.cola.rpcspringbootstarter.annotation;

import com.cola.rpc.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者注解（用于注册服务）
 * @author Maobohe
 * @createData 2024/4/2 19:46
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    /**
     * 服务类接口
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务版本号
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
}
