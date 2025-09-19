package com.cola.rpc.registry;

import com.cola.rpc.spi.SpiLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * 注册中心工厂（用于获取注册中心对象）
 * @author Maobohe
 * @createData 2024/3/23 20:26
 */
public class RegistryFactory {


    /**
     * 默认注册中心
     */
    private static final String DEFAULT_REGISTRY = RegistryKeys.ETCD;

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        if (StringUtils.isBlank(key)) {
            key = DEFAULT_REGISTRY;
        }
        key = StringUtils.lowerCase(key);
        return SpiLoader.getInstance(Registry.class, key);
    }
}
