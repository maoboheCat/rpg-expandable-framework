package com.cola.examplespringbootprovider;

import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;
import com.cola.rpcspringbootstarter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类（测试）
 * @author Maobohe
 * @createData 2024/4/3 17:00
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名: " + user.getName());
        return user;
    }
}
