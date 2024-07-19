package com.nxj.rpc.fault.tolerance;

import com.nxj.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 故障转移
 */
public class FailOver implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        return null;
    }
}
