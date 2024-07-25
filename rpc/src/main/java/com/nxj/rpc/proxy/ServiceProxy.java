package com.nxj.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.nxj.rpc.RpcApplication;
import com.nxj.rpc.bootstrap.config.RpcConfig;
import com.nxj.rpc.constant.RpcConstant;
import com.nxj.rpc.fault.retry.RetryStrategy;
import com.nxj.rpc.fault.retry.RetryStrategyFactory;
import com.nxj.rpc.fault.tolerance.TolerantStrategy;
import com.nxj.rpc.fault.tolerance.TolerantStrategyFactory;
import com.nxj.rpc.loadbalance.LoadBalancer;
import com.nxj.rpc.loadbalance.LoadBalancerFactory;
import com.nxj.rpc.model.RpcRequest;
import com.nxj.rpc.model.RpcResponse;
import com.nxj.rpc.model.ServiceMetaInfo;
import com.nxj.rpc.registry.Registry;
import com.nxj.rpc.registry.RegistryFactory;
import com.nxj.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalance());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        // 发送 Tcp 请求
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
        } catch (Exception e) {
            // 容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();
    }
}
