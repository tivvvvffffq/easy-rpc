package com.nxj.rpc.loadbalance;

import com.nxj.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器
 */
public interface LoadBalancer {
    /**
     * 选择服务调用
     * @param requestParams
     * @param serviceMetaInfos
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos);
}
