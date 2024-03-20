package com.cola.example.consumer;

import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;
import com.cola.rpc.config.RpcConfig;
import com.cola.rpc.proxy.ServiceProxyFactory;
import com.cola.rpc.utils.ConfigUtils;

/**
 * @author Maobohe
 * @createData 2024/3/19 20:05
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
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
