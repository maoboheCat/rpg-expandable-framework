package com.cola.example.common.service;

import com.cola.example.common.model.User;

/**
 * 用户服务
 * @author Maobohe
 * @createData 2024/3/17 20:21
 */
public interface UserService {

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);
}
