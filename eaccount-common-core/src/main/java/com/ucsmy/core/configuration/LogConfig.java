package com.ucsmy.core.configuration;

import com.ucsmy.core.tool.log.LoggerOperation;
import org.springframework.context.annotation.Bean;

public interface LogConfig {

    @Bean
    default LoggerOperation loggerOperation() {
        return new LoggerOperation();
    }

}
