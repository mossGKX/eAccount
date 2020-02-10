package com.ucsmy.core.tool.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ucsmy.security")
public class SecurityProperties {
    private int invalidTime = 30 * 60;
    private int defaultLevel = -1;
    private String namespace = "";
}
