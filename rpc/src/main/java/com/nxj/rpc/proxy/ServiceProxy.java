package com.nxj.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.nxj.rpc.RpcApplication;
import com.nxj.rpc.config.RpcConfig;
import com.nxj.rpc.constant.RpcConstant;
import com.nxj.rpc.model.RpcRequest;
import com.nxj.rpc.model.RpcResponse;
import com.nxj.rpc.model.ServiceMetaInfo;
import com.nxj.rpc.protocol.*;
import com.nxj.rpc.registry.Registry;
import com.nxj.rpc.registry.RegistryFactory;
import com.nxj.rpc.serializer.Serializer;
import com.nxj.rpc.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     * 当用户调用某个接口的某个方法时，会转为调用invoke方法，原信息都保存在参数列表里
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取 provider 的地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("暂无服务地址");
            }

            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);

            // 发送 TCP 请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()) {
                            System.out.println("Connected to TCP server.");
                            NetSocket socket = result.result();
                            // 构造消息
                            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);

                            // 发送请求
                            try {
                                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encodeBuffer);
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码错误");
                            }

                            // 接收响应
                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                } catch (IOException e) {
                                    throw new RuntimeException("协议消息解码错误");
                                }
                            });
                        }else {
                            System.err.println("Failed to connect to TCP server.");
                        }
                    });
            RpcResponse rpcResponse = responseFuture.get();

            netClient.close();
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
