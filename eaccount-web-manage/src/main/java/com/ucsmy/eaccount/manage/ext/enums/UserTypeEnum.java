package com.ucsmy.eaccount.manage.ext.enums;

/**
 * 用户类型
 *
 * @author ucs_gaokx
 * @since 2017/9/14
 */

public enum UserTypeEnum {

    EMPLOYER("employer"),
    USER("user"),
    MERCHANT("merchant");

    public String value;

    UserTypeEnum(String value) {
        this.value = value;
    }
}
