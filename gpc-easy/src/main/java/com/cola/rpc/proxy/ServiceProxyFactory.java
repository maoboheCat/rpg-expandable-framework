package com.cola.rpc.proxy;

import com.cola.rpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂（用于创建代理对象）
 * @author Maobohe
 * @createData 2024/3/19 11:25
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] {serviceClass},
                new ServiceProxy()
        );
    }

    /**
     * 根据服务类获取Mock 对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] {serviceClass},
                new MockServiceProxy()
        );
    }
}
