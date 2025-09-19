package com.cola.rpc.fault.tolerant;

import com.cola.rpc.spi.SpiLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * 容错策略工厂
 * @author Maobohe
 * @createData 2024/4/1 19:50
 */
public class TolerantStrategyFactory {

    /**
     * 默认容错策略
     */
    private static final String DEFAULT_TOLERANT_STRATEGY  = TolerantStrategyKey.FAIL_BACK;

    public static TolerantStrategy getInstance(String key) {
        if (StringUtils.isBlank(key)) {
            key = DEFAULT_TOLERANT_STRATEGY;
        }
        key = StringUtils.lowerCase(key);
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
