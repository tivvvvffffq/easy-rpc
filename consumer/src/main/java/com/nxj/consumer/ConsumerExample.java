package com.nxj.consumer;

import com.nxj.common.model.User;
import com.nxj.common.service.UserService;
import com.nxj.rpc.proxy.ServiceProxy;
import com.nxj.rpc.proxy.ServiceProxyFactory;

/**
 * 服务消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("tiv");

        // 调用服务
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null!");
        }
    }
}
