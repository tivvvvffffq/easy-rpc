package com.nxj.provider;

import com.nxj.common.service.UserService;
import com.nxj.registry.LocalRegistry;
import com.nxj.server.HttpServer;
import com.nxj.server.VertxHttpServer;

/**
 * 服务提供者示例
 */
public class ProviderExample {
    public static void main(String[] args) {

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
