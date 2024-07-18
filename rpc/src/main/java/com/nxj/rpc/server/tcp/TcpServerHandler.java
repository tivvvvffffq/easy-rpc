package com.nxj.rpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

/**
 * TCP 请求处理器
 */
public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 处理请求
        });
        netSocket.handler(tcpBufferHandlerWrapper);
    }
}
