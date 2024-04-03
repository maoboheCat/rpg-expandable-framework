package com.cola.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.cola.rpc.RpcApplication;
import com.cola.rpc.exception.ErrorCode;
import com.cola.rpc.exception.RpcException;
import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vertx TCP 请求客户端
 *
 * @author Maobohe
 * @createData 2024/3/29 9:06
 */
@Slf4j
public class VertxTcpClient {

    private static Buffer encode;

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {
            if (!result.succeeded()) {
                log.info("Failed to connect to Server");
            }
            log.info("Connect to TCP server");
            NetSocket socket = result.result();
            // 发送数据
            // 构造信息
            ProtocolMessage<Object> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);

            // 编码请求
            try {
                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encode);
            } catch (IOException e) {
                throw new RpcException(ErrorCode.PROTOCOL_ENCODE_ERROR, "协议编码错误");
            }

            // 接收响应
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RpcException(ErrorCode.PROTOCOL_ENCODE_ERROR);
                }
            });
            socket.handler(bufferHandlerWrapper);
        });
        RpcResponse rpcResponse = responseFuture.get();
        netClient.close();
        return rpcResponse;
    }
}
