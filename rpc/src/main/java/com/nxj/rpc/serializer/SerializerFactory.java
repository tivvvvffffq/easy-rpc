package com.nxj.rpc.serializer;

import com.nxj.rpc.spi.SpiLoader;

/**
 * 序列化器工厂
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器-原生JDK
     */
    private static final Serializer DEFAULT_SERIALIZER = new JDKSerializer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
