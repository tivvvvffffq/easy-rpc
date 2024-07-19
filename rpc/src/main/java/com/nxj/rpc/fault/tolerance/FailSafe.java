package com.nxj.rpc.fault.tolerance;

import com.nxj.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理
 */
@Slf4j
public class FailSafe implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("failsafe ", e);
        return new RpcResponse();
    }
}
