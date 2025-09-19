package com.cola.rpc.serializer;

import com.cola.rpc.spi.SpiLoader;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂
 * @author Maobohe
 * @createData 2024/3/20 20:38
 */
public class SerializerFactory {

    /**
     * 默认序列化器
     */
    private static final String DEFAULT_SERIALIZER = SerializerKey.JDK;

    public static Serializer getInstance(String key) {
        // 增加降级处理逻辑
        if (StringUtils.isBlank(key)) {
            key = DEFAULT_SERIALIZER;
        }
        key = StringUtils.lowerCase(key);
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
