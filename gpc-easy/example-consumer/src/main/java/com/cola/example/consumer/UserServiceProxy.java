package com.cola.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cola.example.common.model.User;
import com.cola.example.common.service.UserService;
import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.model.RpcResponse;
import com.cola.rpc.serializer.JdkSerializer;
import com.cola.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * 静态代理
 *
 * @author Maobohe
 * @createData 2024/3/19 10:43
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();
        // 发送请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
