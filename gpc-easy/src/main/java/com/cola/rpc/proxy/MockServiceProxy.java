package com.cola.rpc.proxy;

import com.cola.example.common.model.User;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Mock 服务代理（JDK 动态代理）
 * @author Maobohe
 * @createData 2024/3/19 20:52
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    private Faker faker = new Faker(Locale.CHINA);

    /**
     * 调用代理
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("------> mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 生成指定类型的默认值对象
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type) {
        // 基本类型
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            }
        }
        // 使用faker生成假数据
        if (type.equals(User.class)) {
            User user = new User();
            user.setName(faker.name().fullName());
            return user;
        }
        // 对象类型
        return null;
    }
}
