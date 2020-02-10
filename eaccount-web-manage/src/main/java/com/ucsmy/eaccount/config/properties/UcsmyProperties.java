package com.ucsmy.eaccount.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ucsmy")
public class UcsmyProperties {
    private Redirect redirect = new Redirect();
    private String initPassword = "123456";// 初始用户密码，配置文件修改
    private boolean genCode = false;// 是否开启代码生成功能
}
