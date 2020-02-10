package com.ucsmy.core.tool.serialNumber.utils;

import com.ucsmy.core.tool.serialNumber.generator.AbstractSerialNumberGenerator;
import com.ucsmy.core.utils.ApplicationContextUtils;

/**
 * 提供访问Spring上下文的bean，让工具类的Static方法可以使用来获取配置里的serialNumberGenerator
 * Created by ucs on 2017/5/26.
 */
public class SerialNumberContextHelper {

    /**
     * 流水号长度
     */
    public static final int SERIALNUMBER_LENGTH = 18;

    /**
     * 默认的流水号Bean Name
     */
    private static final String GENERATOR_NAME = "serialNumberGenerator";

    /**
     * 备用的流水号Bean Name
     */
    private static final String BACKUP_GENERATOR_NAME = "backupSerialNumberGenerator";

    /**
     * 获取配置里的流水号生成器
     *
     * @return
     */
    public static AbstractSerialNumberGenerator getSerialNumberGenerator() {
        return getBean(GENERATOR_NAME);
    }

    /**
     * 获取配置里的流水号生成器
     *
     * @return
     */
    public static AbstractSerialNumberGenerator getBackupSerialNumberGenerator() {
        return getBean(BACKUP_GENERATOR_NAME);
    }

    private static AbstractSerialNumberGenerator getBean(String name) {
        return (AbstractSerialNumberGenerator) ApplicationContextUtils.getBean(name);
    }

}
