package com.cola.rpc.exception;

/**
 * Rpc 自定义异常类
 * @author Maobohe
 * @createData 2024/4/3 19:24
 */
public class RpcException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RpcException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public RpcException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
