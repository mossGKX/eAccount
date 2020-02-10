package com.ucsmy.eaccount.config;

import com.ucsmy.core.configuration.*;
import org.springframework.context.annotation.Configuration;

/**
 * 引入初始化bean/序列化/日志/redis/Security/验证码配置
 */
@Configuration
public class InitConfig implements InitializationConfig, SerialNumberConfig, LogConfig, RedisConfig, SecurityConfig, CaptchaConfig {
}
