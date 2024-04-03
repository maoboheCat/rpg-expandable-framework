package com.cola.rpcspringbootstarter.annotation;

import com.cola.rpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.cola.rpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.cola.rpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用Rpc 注解
 * @author Maobohe
 * @createData 2024/4/2 19:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动server
     */
    boolean needServer() default true;
}
