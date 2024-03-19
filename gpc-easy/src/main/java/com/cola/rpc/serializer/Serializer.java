package com.cola.rpc.serializer;

import java.io.IOException;

/**
 * @author Maobohe
 * @createData 2024/3/18 21:04
 */
public interface Serializer {

    /**
     * 序列化
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
