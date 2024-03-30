package com.cola.rpc.protocol;

import com.cola.rpc.serializer.Serializer;
import com.cola.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;


/**
 * 编码器
 * @author Maobohe
 * @createData 2024/3/29 9:42
 */
public class ProtocolMessageEncoder {

    /**
     * 编码
     * @param protocolMessge
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessge) throws IOException {
        if (protocolMessge == null || protocolMessge.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = protocolMessge.getHeader();
        Buffer buffer = Buffer.buffer();
        // 依次想缓冲区写入字节
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(protocolMessge.getBody());
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }
}
