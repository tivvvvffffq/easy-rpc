package com.nxj.rpc.server.tcp;

import com.nxj.rpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            RecordParser parser = RecordParser.newFixed(8);
            parser.setOutput(new Handler<Buffer>() {
                int size = -1;
                Buffer resultBuffer = Buffer.buffer();
                @Override
                public void handle(Buffer buffer) {
                    if(size == -1) {
                        size = buffer.getInt(4);
                        parser.fixedSizeMode(size);
                        resultBuffer.appendBuffer(buffer);
                    }else {
                        resultBuffer.appendBuffer(buffer);
                        System.out.println(resultBuffer.toString());

                        parser.fixedSizeMode(8);
                        size = -1;
                        resultBuffer = Buffer.buffer();
                    }
                }
            });
            // 处理连接
            socket.handler(parser);
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
