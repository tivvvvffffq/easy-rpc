package com.nxj.rpc.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂
 */
public class SerializerFactory {

    /**
     * 单例序列化器映射
     */
    private static final Map<String, Serializer> KEY_SERIALIZAR_MAP = new HashMap<String, Serializer>() {
        {
            put(SerializerKeys.JDK, new JDKSerializer());
            put(SerializerKeys.JSON, new JsonSerializer());
            put(SerializerKeys.KRYO, new KryoSerializer());
            put(SerializerKeys.HESSIAN, new HessianSerializer());
        }
    };

    /**
     * 默认序列化器-原生JDK
     */
    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZAR_MAP.get(SerializerKeys.JDK);

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return KEY_SERIALIZAR_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}
