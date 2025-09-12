package com.cola.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.constant.RpcConstant;
import com.cola.rpc.exception.ErrorCode;
import com.cola.rpc.exception.RpcException;
import com.cola.rpc.fault.retry.RetryStrategy;
import com.cola.rpc.fault.retry.RetryStrategyFactory;
import com.cola.rpc.fault.tolerant.TolerantStrategy;
import com.cola.rpc.fault.tolerant.TolerantStrategyFactory;
import com.cola.rpc.loadbalancer.LoadBalancer;
import com.cola.rpc.loadbalancer.LoadBalancerFactory;
import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.registry.Registry;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpc.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 从注册中心获取服务提供者地址
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMateInfo = new ServiceMetaInfo();
            serviceMateInfo.setServiceName(serviceName);
            serviceMateInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMateInfoList = registry.serviceDiscovery(serviceMateInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMateInfoList)) {
                throw new RpcException(ErrorCode.PROXY_FAILED_ERROR, "暂无服务地址");
            }
            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            requestParams.put("id", IdUtil.getSnowflakeNextId());
            ServiceMetaInfo selectedServiceMateInfo = loadBalancer.select(requestParams, serviceMateInfoList);
            // 发送TCP请求
            // 使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                VertxTcpClient.doRequest(rpcRequest, selectedServiceMateInfo)
            );
            return rpcResponse.getData();
        } catch (IOException | RuntimeException e) {
            log.error("------> VertxTcpClient 请求失败");
            log.info("------> 容错机制处理中...");
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("serviceName", rpcRequest.getServiceName());
            requestParams.put("methodName", rpcRequest.getMethodName());
            RpcResponse rpcResponse = tolerantStrategy.doTolerant(requestParams, e);
            if (rpcResponse != null) {
                return rpcResponse.getData();
            }
        }
        System.out.println("进入最终处理");
        return null;
    }
}
