package com.cola.rpc.fault.retry.impl;

import com.cola.rpc.fault.retry.RetryStrategy;
import com.cola.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 * @author Maobohe
 * @createData 2024/3/31 16:59
 */
public class NoRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
