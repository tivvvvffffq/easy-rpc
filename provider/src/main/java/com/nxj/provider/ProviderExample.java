package com.nxj.provider;

import com.nxj.common.service.UserService;
import com.nxj.rpc.RpcApplication;
import com.nxj.rpc.registry.LocalRegistry;
import com.nxj.rpc.server.HttpServer;
import com.nxj.rpc.server.VertxHttpServer;

/**
 * 服务提供者示例
 */
public class ProviderExample {
    public static void main(String[] args) {
        // 框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
