package com.cola.rpc.fault.retry;

import com.cola.rpc.spi.SpiLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * 重试机制工厂
 * @author Maobohe
 * @createData 2024/3/31 19:53
 */
public class RetryStrategyFactory {

    /**
     * 默认重试策略
     */
    private static final String DEFAULT_RETRY_STRATEGY = RetryStrategyKey.NO;

    public static RetryStrategy getInstance(String key) {
        if (StringUtils.isBlank(key)) {
            key = DEFAULT_RETRY_STRATEGY;
        }
        key = StringUtils.lowerCase(key);
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}
