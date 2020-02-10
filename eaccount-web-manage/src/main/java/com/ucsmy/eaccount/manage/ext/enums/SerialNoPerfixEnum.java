package com.ucsmy.eaccount.manage.ext.enums;

/**
 * 流水号前缀定义
 *
 * @author ucs_gaokx
 * @since 2017/9/15
 */

public enum SerialNoPerfixEnum {

    EC_USERINFO_ID("1001"),
    EC_USERACCOUNT("1002"),
    EC_USERNO("1003"),
    EC_ACCOUNT_ID("1004");

    public String prefix;

    SerialNoPerfixEnum(String prefix) {
        this.prefix = prefix;
    }
}