package com.cola.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.constant.RpcConstant;
import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.loadbalancer.LoadBalancerFactory;
import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.protocol.*;
import com.cola.rpc.registry.Registry;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpc.serializer.Serializer;
import com.cola.rpc.serializer.SerializerFactory;
import com.cola.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 * @author Maobohe
 * @createData 2024/3/19 11:11
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     * @param proxy
     * @param method
     * @param args
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        String serviceName = method.getDeclaringClass().getName();
        final Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());
        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务提供者地址
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMateInfo = new ServiceMetaInfo();
            serviceMateInfo.setServiceName(serviceName);
            serviceMateInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMateInfoList = registry.serviceDiscovery(serviceMateInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMateInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMateInfo = loadBalancer.select(requestParams, serviceMateInfoList);
            // 发送TCP请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMateInfo);
            return rpcResponse.getData();
        } catch (IOException | RuntimeException e) {
            log.info("VertxTcpClient 请求失败");
            e.printStackTrace();
        }
        return null;
    }
}
