package com.ucsmy.core.tool.security;

import com.ucsmy.core.utils.RedisUtils;
import com.ucsmy.core.utils.StringUtils;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SecurityService {

    private final static String KEY = "ucsmy:security:service:";

    private final SecurityProperties properties;

    public SecurityService(SecurityProperties properties) {
        this.properties = properties;
    }

    public <T> String genSecurityCode(T data, Business business) {
        return genSecurityCode(business, properties.getDefaultLevel(), data, properties.getInvalidTime());
    }

    public <T> String genSecurityCode(T data, int invalidTime, Business business) {
        return genSecurityCode(business, properties.getDefaultLevel(), data, invalidTime);
    }

    public <T> String  genSecurityCode(Business business, int level, T data) {
        return genSecurityCode(business, level, data, properties.getInvalidTime());
    }

    public <T> String  genSecurityCode(Business business, int level, T data, int invalidTime) {
        Security security = Security.builder()
                .code(StringUtils.getUUID())
                .business(business.toString())
                .level(level)
                .data(data)
                .build();
        RedisUtils.set(getKey(security.getCode()), security, (long) invalidTime);
        return security.getCode();
    }

    public <T> Security<T> getSecurityCode(String code, Business business) {
        return getSecurityCode(code, business, properties.getDefaultLevel());
    }

    public <T> Security<T> getSecurityCode(String code, Business business, int level) {
        if(code == null || business == null)
            return null;
        Security<T> security = RedisUtils.get(getKey(code));
        if(security != null && security.getBusiness().equals(business.toString()) && security.getLevel() == level) {
            return security;
        }
        return null;
    }

    public void deleteSecurityCode(String code) {
        if(code == null)
            return;
        RedisUtils.delete(getKey(code));
    }

    private String getKey(String code) {
        if(StringUtils.isEmpty(properties.getNamespace())) {
            return KEY.concat(code);
        } else {
            return KEY.concat(properties.getNamespace()).concat(":").concat(code);
        }
    }

    public enum Business {
        REG("注册", "reg"),
        FORGET_PWD("找回密码", "update_pwd"),
        UPDATE_PHONE("修改手机", "update_phone"),
        CAPTCHA("验证码", "captcha"),
        PAY("支付", "pay");

        private String value;
        private String name;

        Business(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    @Data
    @NoArgsConstructor
    public static class Security<T> {
        private String code;
        private String business;
        private int level;
        private T data;

        @Builder
        public Security(String code, String business, int level, T data) {
            this.code = code;
            this.business = business;
            this.level = level;
            this.data = data;
        }
    }
}
