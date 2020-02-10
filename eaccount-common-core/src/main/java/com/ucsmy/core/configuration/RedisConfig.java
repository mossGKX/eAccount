package com.ucsmy.core.configuration;

import com.ucsmy.core.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 */
public interface RedisConfig {

    @Bean(name = "jsonRedisTemplate")
    default RedisOperations<String, Object> jsonRedisTemplate(RedisConnectionFactory connectionFactory) {
        JsonRedisTemplate template = new JsonRedisTemplate(connectionFactory);
        RedisUtils.setRedisTemplate(template);
        return template;
    }

    class JsonRedisTemplate extends RedisTemplate<String, Object> {

        JsonRedisTemplate() {
            RedisSerializer<String> stringSerializer = new StringRedisSerializer();
            GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
            setKeySerializer(stringSerializer);
            setValueSerializer(jsonRedisSerializer);
            setHashKeySerializer(stringSerializer);
            setHashValueSerializer(jsonRedisSerializer);
        }

        JsonRedisTemplate(RedisConnectionFactory connectionFactory) {
            this();
            setConnectionFactory(connectionFactory);
            afterPropertiesSet();
        }

    }
}
