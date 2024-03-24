package com.cola.rpc.registry;

import com.cola.rpc.spi.SpiLoader;

/**
 * 注册中心工厂（用于获取注册中心对象）
 * @author Maobohe
 * @createData 2024/3/23 20:26
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Regisrty.class);
    }

    /**
     * 默认注册中心
     */
    private static final Regisrty DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Regisrty getInstance(String key) {
        return SpiLoader.getInstance(Regisrty.class, key);
    }
}
