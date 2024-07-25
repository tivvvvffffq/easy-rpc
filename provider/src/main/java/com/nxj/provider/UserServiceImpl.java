package com.nxj.provider;

import com.nxj.common.model.User;
import com.nxj.common.service.UserService;
import com.nxj.rpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户的名字是: " + user.getName());
        return user;
    }
}
