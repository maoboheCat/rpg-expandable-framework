package com.cola.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cola.rpc.RpcApplication;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.constant.RpcConstant;
import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.registry.Regisrty;
import com.cola.rpc.registry.RegistryFactory;
import com.cola.rpc.serializer.Serializer;
import com.cola.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务代理（JDK 动态代理）
 * @author Maobohe
 * @createData 2024/3/19 11:11
 */
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
            Regisrty regisrty = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMateInfo = new ServiceMetaInfo();
            serviceMateInfo.setServiceName(serviceName);
            serviceMateInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMateInfoList = regisrty.serviceDiscovery(serviceMateInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMateInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            // todo 暂时先取第一个
            ServiceMetaInfo selectedServiceMateInfo = serviceMateInfoList.get(0);
            // 发送请求
            // todo 地址被硬编码（使用注册中心和服务发现机制解决）
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMateInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
