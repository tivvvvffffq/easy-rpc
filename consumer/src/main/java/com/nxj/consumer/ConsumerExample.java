package com.nxj.consumer;

import com.nxj.common.model.User;
import com.nxj.common.service.UserService;
import com.nxj.rpc.config.RpcConfig;
import com.nxj.rpc.proxy.ServiceProxyFactory;
import com.nxj.rpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "easy-rpc");
        System.out.println(rpc.getName());

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

        System.out.println(userService.getNumber());
    }
}
