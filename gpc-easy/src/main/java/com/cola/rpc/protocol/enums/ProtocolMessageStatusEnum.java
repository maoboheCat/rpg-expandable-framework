package com.cola.rpc.protocol.enums;

import lombok.Getter;

/**
 * 协议信息的状态枚举
 * @author Maobohe
 * @createData 2024/3/26 20:19
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok", 20),
    BAD_REQUEST("badRequest", 30),
    BAD_RESPONSE("badResponse", 40);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value 获取枚举值
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
