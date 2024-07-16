package com.nxj.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

public class VertxTcpClient {
    public void start() {
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888, "localhost", result -> {
            if(result.succeeded()) {
                System.out.println("Connected to TCP server.");
                NetSocket socket = result.result();
                // 发送数据
                socket.write("Hello, server.");
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.err.println("Failed to connect to TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}