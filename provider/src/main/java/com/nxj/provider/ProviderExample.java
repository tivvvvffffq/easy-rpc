package com.nxj.provider;

import com.nxj.common.service.UserService;
import com.nxj.rpc.RpcApplication;
import com.nxj.rpc.config.RegistryConfig;
import com.nxj.rpc.config.RpcConfig;
import com.nxj.rpc.model.ServiceMetaInfo;
import com.nxj.rpc.registry.LocalRegistry;
import com.nxj.rpc.registry.Registry;
import com.nxj.rpc.registry.RegistryFactory;
import com.nxj.rpc.server.tcp.VertxTcpServer;

/**
 * 服务提供者示例
 */
public class ProviderExample {
    public static void main(String[] args) {
        // 框架初始化
        RpcApplication.init();

        // 注册服务到本地
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();

        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 TCP 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
