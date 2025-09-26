package com.cola.rpc.fault.tolerant.impl;

import com.cola.rpc.fault.tolerant.TolerantStrategy;
import com.cola.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 转义到其他服务节点 - 容错策略
 * @author Maobohe
 * @createData 2024/4/1 19:57
 */
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo
        return null;
    }
}
