package com.cola.rpc.fault.retry;

import com.cola.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 * @author Maobohe
 * @createData 2024/3/31 16:37
 */
public interface RetryStrategy {

    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
