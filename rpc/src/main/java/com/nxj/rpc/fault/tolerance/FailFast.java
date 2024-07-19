package com.nxj.rpc.fault.tolerance;

import com.nxj.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略 快速失败
 */
public class FailFast implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // 立即通知调用方异常
        throw new RuntimeException("服务报错 ", e);
    }
}
