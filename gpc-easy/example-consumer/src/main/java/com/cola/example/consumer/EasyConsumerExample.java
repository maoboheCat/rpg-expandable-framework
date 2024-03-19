package com.cola.example.consumer;

import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;
import com.cola.rpc.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者
 * @author Maobohe
 * @createData 2024/3/17 21:34
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // 静态代理
//        UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("cola");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
