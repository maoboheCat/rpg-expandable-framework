package com.cola.rpc.fault.tolerant;

import com.cola.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败 - 立刻通知外层调用方
 * @author Maobohe
 * @createData 2024/4/1 19:28
 */
public class FailFastTolerantStrategy implements TolerantStrategy{

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}
