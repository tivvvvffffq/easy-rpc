package com.nxj.rpc.bootstrap;

import com.nxj.rpc.RpcApplication;
import com.nxj.rpc.config.RegistryConfig;
import com.nxj.rpc.config.RpcConfig;
import com.nxj.rpc.model.ServiceMetaInfo;
import com.nxj.rpc.model.ServiceRegisterInfo;
import com.nxj.rpc.registry.LocalRegistry;
import com.nxj.rpc.registry.Registry;
import com.nxj.rpc.registry.RegistryFactory;
import com.nxj.rpc.server.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 服务提供者初始化
 */
@Slf4j
public class ProviderBootStrap {
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // rpc 框架初始化
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败 ", e);
            }
        }

        // 启动服务器
        log.info("vertx 服务器启动");
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
