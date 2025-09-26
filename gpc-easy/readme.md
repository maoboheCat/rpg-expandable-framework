# RPC框架

框架参考Dubbo RPC框架设计，实现必要的供给框架启用的功能。基于 Java + Vert.x 的高性能 RPC 框架，网络通讯选用Vert.x框架提供的TCP连接，在此基础上自定义RPC协议。基于Etcd和ZooKeeper实现注册中心。基于注解驱动和Spring Boot Starter，开发者可以快速引入 RPC 框架到项目中。



## 项目框架介绍



![](https://pic1.imgdb.cn/item/68d66339c5157e1a8837fc3d.png)



系统核心架构设计分为7个模块

消费者代理模块、注册中心、本地服务注册器、路由模块(负载均衡)、自定义 RPC 协议、请求处理器模块、启动器模块

以一次完整的调用过程为例，讲解模块之间的关系。首先提供者启动 TCP 服务器，并将服务信息注册到本地服务注册器和注册中心。消费者通过代理模块从注册中心获取到服务的调用信息列表，通过负载均衡确定一个调用地址，并构造基于 RPC 协议的请求。请求经过编码器处理后通过TCP客户端发送。提供者接收到请求后通过解码器还原消息，根据消息内容，从本地服务注册器找到服务实现类，通过反射完成调用。将调用结果进行封装，通过编码器处理并返回响应。消费者收到响应后，通过解码器还原消息从而完成一次调用。如果调用失败，消费者会根据预设好的策略处理。



## 项目启动

使用时需要注意，Java的版本需要大于11



### 服务提供者启动

启动etcd注册中心

![](https://pic1.imgdb.cn/item/68d66ef2c5157e1a88385425.png)

引入**@EnableRpc**注解，启动TCP服务器

![](https://pic1.imgdb.cn/item/68d66f9bc5157e1a8838545b.png)

在需要注册服务信息信息的类上引用**@RpcService**注解

![](https://pic1.imgdb.cn/item/68d66ff0c5157e1a8838546d.png)

启动成功后，会提示下方信息

![](https://pic1.imgdb.cn/item/68d6708cc5157e1a8838549b.png)



### 服务消费者启动

引入**@EnableRpc**注解，属性必须为false，只初始化框架

![](https://pic1.imgdb.cn/item/68d670cbc5157e1a883854a1.png)

在需要使用服务的类上使用

![](https://pic1.imgdb.cn/item/68d67111c5157e1a883854b7.png)

启动成功后，出现提示作为客户端运行

![](https://pic1.imgdb.cn/item/68d6873dc5157e1a883862ce.png)

### ETCD中的存储

![](https://pic1.imgdb.cn/item/68d6873dc5157e1a883862ce.png)