package com.ucsmy.core.tool.serialNumber.generator;

import com.ucsmy.core.tool.serialNumber.bean.GeneratorMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 序列号生成器 - Redis模式
 * Created by ucs on 2017/5/18.
 */
public class SerialNumberRedisGenerator extends AbstractSerialNumberGenerator {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 命名空间
     */
    private String namespace;


    public SerialNumberRedisGenerator(String prefix, int startNum, String dataFormat, String namespace) {
        super(prefix, startNum, dataFormat);
        this.namespace = namespace;
    }

    @Override
    public GeneratorMode getMode() {
        return GeneratorMode.REDIS;
    }

    @Override
    public String get(String prefix) {
        Date now = new Date();
        DateFormat dateFormat = super.getDateFormat();
        StringBuilder key = new StringBuilder(super.getPrefix());
        key.append(":").append(dateFormat.format(now));
        // 目前设置过期时间为一天
        Long number = increment(key.toString(), 1L, 1, TimeUnit.DAYS);
        return super.generateNumber(prefix, now, number);
    }

    /**
     * 生成指定长度的序列号，特定业务使用，用于生成非前缀+日期+流水号格式的序号
     * @param prefix 业务前缀
     * @param length 需要生成的长度
     * @param maxLength 序列号最大长度
     * @param delta 增量
     * @return
     */
    public String getAutoIncrement(String prefix, int length, int maxLength, long delta) {
        Long value = increment(prefix, delta);
        return super.parseAutoIncrement(value, length, maxLength);
    }

    /**
     * redis 增量
     * @param key 主键
     * @param delta 增量
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return
     */
    private Long increment(String key, long delta, long timeout, TimeUnit unit) {
        Long value = increment(key, delta);
        // 检查过期时间
        long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (expire < 0) {
            // 添加过期时间
            stringRedisTemplate.expire(key, timeout, unit);
        }
        return value;
    }

    private Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(namespace.concat(":").concat(key), delta);
    }
}
