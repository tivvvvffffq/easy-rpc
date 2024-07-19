package com.nxj.rpc.fault.tolerance;

import com.nxj.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 服务降级
 */
public class FailBack implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        return null;
    }
}
