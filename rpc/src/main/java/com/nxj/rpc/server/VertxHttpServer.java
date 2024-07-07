package com.nxj.rpc.server;

import io.vertx.core.Vertx;

/**
 * 基于Vert.x的Http服务器
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        // 获取Vert.x实例，创建Http服务器
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 处理请求
        server.requestHandler(request -> {
            // 处理Http请求
            System.out.println("Received request: " + request.method() + " " + request.uri());

            // 发送Http响应
            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x Http Server.");
        });

        // 启动服务器，监听指定端口
        server.listen(port, result -> {
            if(result.succeeded()) {
                System.out.println("Server is listening on port " + port);
            }else {
                System.out.println("Failed to start server " + result.cause());
            }
        });


    }
}
