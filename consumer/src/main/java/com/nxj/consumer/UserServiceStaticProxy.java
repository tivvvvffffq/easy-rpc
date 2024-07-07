package com.nxj.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.nxj.common.model.User;
import com.nxj.common.service.UserService;
import com.nxj.rpc.model.RpcRequest;
import com.nxj.rpc.model.RpcResponse;
import com.nxj.rpc.serializer.JDKSerializer;
import com.nxj.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * 静态代理
 */
public class UserServiceStaticProxy implements UserService {
    @Override
    public User getUser(User user) {
        Serializer serializer = new JDKSerializer();

        // 构造rpc请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        // 发送请求
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(bodyBytes).execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
