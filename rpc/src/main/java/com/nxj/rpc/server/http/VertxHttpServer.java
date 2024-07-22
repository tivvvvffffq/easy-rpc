package com.nxj.rpc.server.http;

import io.vertx.core.Vertx;

/**
 * 基于 Vert.x 的 Http 服务器
 */
public class VertxHttpServer implements HttpServer{
    /**
     * 启动服务器
     * @param port
     */
    @Override
    public void doStart(int port) {
        // 获取Vert.x实例，创建Http服务器
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动服务器，监听指定端口
        server.listen(port, result -> {
            if(result.succeeded()) {
                System.out.println("Server is listening on port " + port);
            }else {
                System.err.println("Failed to start server " + result.cause());
            }
        });
    }
}
