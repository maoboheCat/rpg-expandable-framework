package com.cola.rpc.server.tcp;

import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.protocol.*;
import com.cola.rpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Tcp 请求处理
 * @author Maobohe
 * @createData 2024/3/29 11:29
 */
public class TcpServerHandler implements Handler<NetSocket> {

    public void handle(NetSocket netSocket) {
        // 处理连接
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接受请求，解码
            ProtocolMessage<RpcRequest> protocolMessage = null;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();
            // 处理请求
            // 构造响应体
            RpcResponse rpcResponse = new RpcResponse();
            // 构建协议信息
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());

            // 请求为空
            if (rpcRequest == null) {
                rpcResponse.setMessage("request is null");
                ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
                try {
                    Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                    netSocket.write(encode);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException("协议信息编码失败");
                }
            }
            // 请求不为空
            try {
                // 获取想要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            // 响应，编码
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议信息编码失败");
            }
        });
        netSocket.handler(bufferHandlerWrapper);
    }
}
