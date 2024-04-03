package com.cola.example.consumer;

import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;
import com.cola.rpc.bootstrap.ConsumerBootstrap;
import com.cola.rpc.proxy.ServiceProxyFactory;


/**
 * @author Maobohe
 * @createData 2024/3/19 20:05
 */
public class ConsumerExample {

    public static void main(String[] args) {
        ConsumerBootstrap.init();
        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("cola");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }
}
