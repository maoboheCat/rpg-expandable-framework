package com.cola.rpc.fault.tolerant.impl;

import com.cola.rpc.exception.ErrorCode;
import com.cola.rpc.exception.RpcException;
import com.cola.rpc.fault.tolerant.TolerantStrategy;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.proxy.MockServiceProxy;
import com.cola.rpc.proxy.ServiceProxyFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 降级到其他服务 - 故障恢复
 * @author Maobohe
 * @createData 2024/4/1 19:56
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("降级处理中...");
        try {
            Class<?> serviceName = Class.forName(context.get("serviceName").toString());
            String methodName = context.get("methodName").toString();
            Method[] methods = serviceName.getMethods();
            int index = 0;
            for (; index < methods.length; index++) {
                if (methods[index].getName().equals(methodName)) {
                    break;
                }
            }
            if (index == methods.length) {
                throw new RpcException(ErrorCode.REGISTRY_OTHER_ERROR);
            }
            Object result = new MockServiceProxy().invoke(ServiceProxyFactory.getMockProxy(serviceName),
                    methods[index], new Object[]{});
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setData(result);
            return rpcResponse;
        } catch (Throwable ex) {
            throw new RpcException(ErrorCode.REGISTRY_OTHER_ERROR);
        }
    }
}
