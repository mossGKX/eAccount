package com.ucsmy.eaccount.manage.ext.enums;

/**
 * 用户类型
 *
 * @author ucs_gaokx
 * @since 2017/9/14
 */

public enum AccountTypeEnum {

    /* 白条账户 */
    BAITIAO("baitiao"),
    /* 资金账户 */
    FUND("fund");

    public String value;

    AccountTypeEnum(String value) {
        this.value = value;
    }
}
