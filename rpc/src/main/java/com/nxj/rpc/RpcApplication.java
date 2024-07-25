package com.nxj.rpc;

import com.nxj.rpc.bootstrap.config.RegistryConfig;
import com.nxj.rpc.bootstrap.config.RpcConfig;
import com.nxj.rpc.constant.RpcConstant;
import com.nxj.rpc.registry.Registry;
import com.nxj.rpc.registry.RegistryFactory;
import com.nxj.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC 框架应用
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 自定义配置初始化
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", rpcConfig.toString());

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);

        // 创建 shutdown hook，jvm退出时会自动执行 registry 的 destroy 方法
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 加载配置文件初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        // 尝试加载配置
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 加载失败
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取 RPC 配置
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if(rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
