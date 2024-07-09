package com.nxj.rpc.config;

import lombok.Data;

/**
 * RPC框架配置
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "easy-rpc";
    /**
     * 版本
     */
    private String version = "1.0";
    /**
     * 服务器主机
     */
    private String serverHost = "localhost";
    /**
     * 服务器端口
     */
    private Integer serverPort = 8080;
    /**
     * 是否开启模拟调用
     */
    private boolean mock = false;
}
