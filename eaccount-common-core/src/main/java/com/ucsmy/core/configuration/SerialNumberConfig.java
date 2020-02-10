package com.ucsmy.core.configuration;

import com.ucsmy.core.tool.serialNumber.bean.SerialNumberProperties;
import com.ucsmy.core.tool.serialNumber.utils.SerialNumberContextHelper;
import com.ucsmy.core.tool.serialNumber.exception.SerialNumberConfigException;
import com.ucsmy.core.tool.serialNumber.generator.AbstractSerialNumberGenerator;
import com.ucsmy.core.tool.serialNumber.generator.SerialNumberDefaultGenerator;
import com.ucsmy.core.tool.serialNumber.generator.SerialNumberRedisGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * 序列号生成器配置文件
 * Created by ucs on 2017/5/17.
 */
public interface SerialNumberConfig {

    @Bean
    default SerialNumberProperties properties() {
        return new SerialNumberProperties();
    }

    /**
     * redies自增序列号生成器
     */
    @ConditionalOnProperty(
            prefix = "ucsmy.serialNumber",
            name = {"mode"},
            havingValue = "REDIS"
    )
    @ConditionalOnClass({RedisTemplate.class})
    @Bean("serialNumberGenerator")
    @ConditionalOnMissingBean
    default AbstractSerialNumberGenerator redisSerialNumberGenerator(SerialNumberProperties properties) {
        // 检查序列号长度是否合法
        checkSerialNumberLength(properties);
        if (StringUtils.isEmpty(properties.getNamespace())) {
            throw new SerialNumberConfigException("SerialNumber Redis Mode lacks of namespace.");
        }
        return new SerialNumberRedisGenerator(properties.getPrefix(), properties.getStartNum(), properties.getDatePattern(), properties.getNamespace());
    }

    /**
     * 备用序列号生成器
     *
     * @return
     */
    @Bean("backupSerialNumberGenerator")
    default AbstractSerialNumberGenerator initBackupSeralNumberGetter(SerialNumberProperties properties) {
        return new SerialNumberDefaultGenerator(properties.getBackupPrefix(), properties.getStartNum(), properties.getDatePattern());
    }

    /**
     * 检查配置里的序列号/备用序列号长度是否为18位
     */
    default void checkSerialNumberLength(SerialNumberProperties properties) {
        StringBuilder key = new StringBuilder();
        key.append(properties.getPrefix())
                .append(properties.getDatePattern())
                .append(properties.getNumberPattern());
        if (key.length() != SerialNumberContextHelper.SERIALNUMBER_LENGTH) {
            throw new SerialNumberConfigException("SerialNumber length Illegal");
        }
        StringBuilder backupKey = new StringBuilder();
        backupKey.append(properties.getBackupPrefix())
                .append(properties.getDatePattern())
                .append(properties.getNumberPattern());
        if (backupKey.length() != SerialNumberContextHelper.SERIALNUMBER_LENGTH) {
            throw new SerialNumberConfigException("Backup serialNumber length Illegal");
        }
    }

}
