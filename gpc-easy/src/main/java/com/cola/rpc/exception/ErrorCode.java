package com.cola.rpc.exception;

/**
 * 错误码
 * @author Maobohe
 * @createData 2024/4/3 19:20
 */
public enum ErrorCode {

    SPI_LOAD_ERROR(10001, "SPI错误"),
    REGISTRY_REGISTER_ERROR(20001, "注册中心注册服务失败"),
    REGISTRY_DISCOVERY_ERROR(20002, "注册中心发现服务失败"),
    REGISTRY_DESTROY_ERROR(20003, "注册中心发现销毁失败"),
    REGISTRY_OTHER_ERROR(20004, ""),
    PROXY_FAILED_ERROR(30001, "代理失败"),
    PROTOCOL_ENCODE_ERROR(40001, "协议编码错误"),
    PROTOCOL_DECODER_ERROR(40002, "协议解码错误"),
    PROTOCOL_ILLEGAL_ERROR(40003, "非法协议消息"),
    SERIALIZER_ILLEGAL_ERROR(40004, "非法序列化协议");


    /**
     * 状态码
     */
    private final int code;

    /**
     *信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
