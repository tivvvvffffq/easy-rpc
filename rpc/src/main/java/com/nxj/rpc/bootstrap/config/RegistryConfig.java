package com.nxj.rpc.bootstrap.config;

import com.nxj.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * 注册中心配置类
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间 单位 ms
     */
    private Long timeout = 10000L;
}
