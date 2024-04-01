package com.cola.rpc.fault.tolerant;

import com.cola.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 * @author Maobohe
 * @createData 2024/4/1 19:23
 */
public interface TolerantStrategy {

    /**
     * 容错
     * @param context
     * @param e
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);
}
