package com.nxj.consumer;

import com.nxj.common.model.User;
import com.nxj.common.service.UserService;
import com.nxj.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * 注解开发消费者示例
 */
@Service
public class UserServiceConsumer {

    /**
     * 使用 Rpc 框架注入
     */
    @RpcReference
    private UserService userService;

    /**
     * 测试
     */
    public void test() {
        User user = new User();
        user.setName("tivvvv");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
