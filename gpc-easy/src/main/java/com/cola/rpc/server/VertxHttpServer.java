package com.cola.rpc.server;

import io.vertx.core.Vertx;


/**
 * @author Maobohe
 * @createData 2024/3/17 20:45
 */
public class VertxHttpServer implements HttpServer{

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());
        // 启用Http服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }
}
