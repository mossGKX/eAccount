package com.ucsmy.core.configuration;

import com.ucsmy.core.tool.security.SecurityProperties;
import com.ucsmy.core.tool.security.SecurityService;
import org.springframework.context.annotation.Bean;

public interface SecurityConfig {

    @Bean(name = "mySecurityProperties")
    default SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    default SecurityService securityService(SecurityProperties properties) {
        return new SecurityService(properties);
    }

}
