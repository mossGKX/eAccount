package com.ucsmy.core.utils;

import com.ucsmy.core.configuration.RedisConfig;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private static RedisConfig.JsonRedisTemplate redisTemplate;
    private static ValueOperations<String, Object> valueOperations;

    public static void setRedisTemplate(RedisConfig.JsonRedisTemplate redisTemplate) {
        if(RedisUtils.redisTemplate == null) {
            RedisUtils.redisTemplate = redisTemplate;
            RedisUtils.valueOperations = redisTemplate.opsForValue();
        }
    }

    public static void set(String key, Object value) {
        valueOperations.set(key, value);
    }

    public static void set(String key, Object value, long invalid) {
        valueOperations.set(key, value, invalid, TimeUnit.SECONDS);
    }

    public static <T> T get(String key) {
        return (T) valueOperations.get(key);
    }

    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static boolean expire(String key, long invalid) {
        return redisTemplate.expire(key, invalid, TimeUnit.SECONDS);
    }

    public static void lPush(String channel, Object message) {
        redisTemplate.opsForList().leftPush(channel, message);
    }

    public static void lSetPush(String channel, Object message) {
        redisTemplate.opsForSet().add(channel, message);
    }

    public static long incr(String key) {
        return valueOperations.increment(key, 1);
    }

    public static long incr(String key, long invalid) {
        long value = valueOperations.increment(key, 1);
        expire(key, invalid);
        return value;
    }
}
