package com.ucsmy.core.common;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/9/18

 * Contributors:
 *      - initial implementation
 */

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 暂无描述
 *
 * @author ucs_gaokuixin
 * @since 2017/9/18
 */
public enum PayWayEnum {

    WEIXIN("微信"),

    ALIPAY("支付宝"),

    BAITIAO("白条支付");

    /** 描述 */
    private String desc;

    private PayWayEnum(String desc) {
        this.desc = desc;
    }

    /**
     * 返回支付方式枚举，默认返回支付宝
     * @param payWay
     * @return
     */
    public static PayWayEnum acquireByPayWay(String payWay) {
        Optional<PayWayEnum> payWayEnum =
                Arrays.stream(PayWayEnum.values())
                        .filter(v -> Objects.equals(v.getDesc(), payWay))
                        .findFirst();
        return payWayEnum.orElse(PayWayEnum.ALIPAY);
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
