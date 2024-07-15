package com.nxj.rpc.server.tcp;

import com.nxj.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {

    /**
     * 处理请求，根据requestData构造Response
     * @param requestData
     * @return
     */
    private byte[] handleRequest(byte[] requestData) {
        return "Hello, client!".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(socket -> {
            // 处理连接
            socket.handler(buffer -> {
                // 处理接收的字节数组
                byte[] requestData = buffer.getBytes();
                System.out.println(buffer.toString());
                byte[] responseData = handleRequest(requestData);
                // 发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });

        // 启动 TCP 服务器
        server.listen(port, result -> {
            if(result.succeeded()) {
                System.out.println("TCP server started on " + port);
            }else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
