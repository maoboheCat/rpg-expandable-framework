package com.cola.example.provider;

import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;

/**
 * 用户服务实现类
 * @author Maobohe
 * @createData 2024/3/17 21:20
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名: " + user.getName());
        return user;
    }
}
