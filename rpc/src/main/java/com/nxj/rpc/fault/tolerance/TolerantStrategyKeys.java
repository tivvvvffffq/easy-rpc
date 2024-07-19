package com.nxj.rpc.fault.tolerance;

/**
 * 容错策略 key
 */
public interface TolerantStrategyKeys {
    String FAIL_SAFE = "failSafe";
    String FAIL_FAST = "failFast";
    String FAIL_BACK = "failBack";
    String FAIL_OVER = "failOver";
}
