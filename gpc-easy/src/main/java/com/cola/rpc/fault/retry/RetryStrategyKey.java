package com.cola.rpc.fault.retry;

/**
 * 重试策略键值
 * @author Maobohe
 * @createData 2024/3/31 19:49
 */
public interface RetryStrategyKey {

    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔重试策略
     */
    String FIXEDINTERVAL = "fixedInterval";
}
