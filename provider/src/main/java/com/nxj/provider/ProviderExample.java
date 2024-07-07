package com.nxj.provider;

import com.nxj.server.HttpServer;
import com.nxj.server.VertxHttpServer;

/**
 * 服务提供者示例
 */
public class ProviderExample {
    public static void main(String[] args) {
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
