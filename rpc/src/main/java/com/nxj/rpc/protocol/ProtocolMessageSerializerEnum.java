package com.nxj.rpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息序列化器枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {
    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian")
    ;

    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 key 获取 Enum
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for(ProtocolMessageSerializerEnum Enum: ProtocolMessageSerializerEnum.values()) {
            if(Enum.key == key) {
                return Enum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取 Enum
     * @param value
     * @return
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        if(ObjectUtil.isEmpty(value)) {
            return null;
        }
        for(ProtocolMessageSerializerEnum anEnum: ProtocolMessageSerializerEnum.values()) {
            if(anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}