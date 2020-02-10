package com.ucsmy.core.utils;

import com.ucsmy.core.tool.serialNumber.utils.SerialNumberContextHelper;
import com.ucsmy.core.tool.serialNumber.generator.AbstractSerialNumberGenerator;

import java.util.UUID;

/**
 * UUID生成器
 * Created by ucs_gaokx on 2017/9/13.
 */
public class SerialNumberGenerator {

    /**
     * 使用jvm uuid生成
     *
     * @return
     */
    public static String jvmGenerate() {
        return UUID.randomUUID().toString();
    }

    /**
     * UUID生成，返回18位的流水号
     *
     * @return
     */
    public static String generate() {
        AbstractSerialNumberGenerator serialNumberGenerator = SerialNumberContextHelper.getSerialNumberGenerator();
        if (serialNumberGenerator == null) {
            return jvmGenerate();
        }
        return serialNumberGenerator.get();
    }

    /**
     * UUID生成，可指定业务前缀，返回18位的流水号
     *
     * @param prefix 自定义的前缀，如果为null，使用默认前缀 <br>
     *               注意：自定义前缀的长度必须小于或等于4，小于时高位补0
     * @return
     */
    public static String generate(String prefix) {
        AbstractSerialNumberGenerator serialNumberGenerator = SerialNumberContextHelper.getSerialNumberGenerator();
        if (serialNumberGenerator == null) {
            return jvmGenerate();
        }
        return serialNumberGenerator.get(prefix);
    }

    /**
     * 生成特定长度的自增流水号，如8位的序列号
     *
     * @param length    流水号长度
     * @param maxLength 流水号最大长度
     * @param prefix    业务标记
     * @param delta     自增量
     * @return
     */
    public static String generate(String prefix, int length, int maxLength, long delta) {
        AbstractSerialNumberGenerator serialNumberGenerator = SerialNumberContextHelper.getSerialNumberGenerator();
        if (serialNumberGenerator == null) {
            return jvmGenerate();
        }
        return serialNumberGenerator.getAutoIncrement(prefix, length, maxLength, delta);
    }
}
