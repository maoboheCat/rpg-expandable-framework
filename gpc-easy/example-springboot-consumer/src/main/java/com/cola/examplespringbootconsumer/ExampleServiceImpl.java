package com.cola.examplespringbootconsumer;

import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;
import com.cola.rpcspringbootstarter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * Springboot starts 任务测试
 * @author Maobohe
 * @createData 2024/4/3 17:11
 */
@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("cola");
        System.out.println("用户名：" + userService.getUser(user).getName());
    }
}
