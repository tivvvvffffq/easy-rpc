package com.nxj.rpc.registry;

import com.nxj.rpc.bootstrap.config.RegistryConfig;
import com.nxj.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心接口
 */
public interface Registry {

    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 消费者监听
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);
}
