package com.nxj.provider;

import com.nxj.common.model.User;
import com.nxj.common.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户: " + user.getName());
        return user;
    }
}
