package com.cola.rpc.server;

/**
 * Http 接口服务
 * @author Maobohe
 * @createData 2024/3/17 20:44
 */
public interface HttpServer {
    /**
     * 启动服务器
     * @param port
     */
    void doStart(int port);
}
