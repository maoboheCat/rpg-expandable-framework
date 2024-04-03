package com.cola.rpcspringbootstarter.bootstrap;

import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.server.tcp.VertxTcpServer;
import com.cola.rpcspringbootstarter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc 框架启动
 * @author Maobohe
 * @createData 2024/4/2 20:21
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    /**
     * Spring 初始化时执行，初始化RPC 框架
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取Enable 注解的属性
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");
        // RPC 框架初始化
        RpcApplication.init();
        // 全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动RPC Service");
        }
    }

}
