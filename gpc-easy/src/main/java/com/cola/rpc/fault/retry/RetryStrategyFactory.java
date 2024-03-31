package com.cola.rpc.fault.retry;

import com.cola.rpc.spi.SpiLoader;

/**
 * 重试机制工厂
 * @author Maobohe
 * @createData 2024/3/31 19:53
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试策略
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}
