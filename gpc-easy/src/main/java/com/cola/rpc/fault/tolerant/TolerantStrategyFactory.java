package com.cola.rpc.fault.tolerant;

import com.cola.rpc.spi.SpiLoader;

/**
 * 容错策略工厂
 * @author Maobohe
 * @createData 2024/4/1 19:50
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     */
    private static TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailFastTolerantStrategy();

    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
