package com.cola.rpc.registry.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.cola.rpc.config.RegistryConfig;
import com.cola.rpc.exception.ErrorCode;
import com.cola.rpc.exception.RpcException;
import com.cola.rpc.model.ServiceMetaInfo;
import com.cola.rpc.registry.Registry;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cola.rpc.exception.ErrorCode.REGISTRY_DISCOVERY_ERROR;

/**
 * etcd 注册中心
 * @author Maobohe
 * @createData 2024/3/23 10:28
 */
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * 本地注册的节点Key集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * 根节点
     */
    public static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMateInfo) throws Exception {
        // 创建Lease客户端
        Lease leaseClient = client.getLeaseClient();
        // 创建30s 的租约
        long leaseId = leaseClient.grant(30).get().getID();
        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMateInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMateInfo), StandardCharsets.UTF_8);
        // 将键值对和租约关联起来
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        // 添加节点信息到本地缓存
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMateInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMateInfo.getServiceNodeKey();
        kvClient.delete( ByteSequence.from(registerKey, StandardCharsets.UTF_8));
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 优先从缓存获取服务
        List<ServiceMetaInfo> cachedServiceMetaInfoList = registryServiceCache.readCache(serviceKey);
        if (cachedServiceMetaInfoList != null) {
            return cachedServiceMetaInfoList;
        }
        // 前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList =  keyValues.stream().map(keyValue -> {
                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                // 监听key的变化
                watch(serviceKey, key);
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
            // 写入服务缓存
            registryServiceCache.writeCache(serviceKey, serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RpcException(REGISTRY_DISCOVERY_ERROR, "获取列表失败 "+ e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        // 下线节点
        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RpcException(ErrorCode.REGISTRY_DESTROY_ERROR, key + "节点下线失败");
            }
        }
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本节点所有的key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（续约）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RpcException(ErrorCode.REGISTRY_OTHER_ERROR, key + "续约失败" + e);
                    }
                }
            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceKey, String serviceModeKey) {
        Watch watchClient = client.getWatchClient();
        // 之前未被监听，开启监听
        boolean newWatch = watchingKeySet.add(serviceModeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceModeKey, StandardCharsets.UTF_8),
                    response -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()) {
                        // key 删除时触发
                        case DELETE:
                        case PUT:
                            // 清理注册服务缓存
                            registryServiceCache.clearCache(serviceKey);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

}
