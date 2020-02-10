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

public enum TradeStatusEnum {
    /**
     * 交易成功
     */
    SUCCESS("交易成功"),

    /**
     * 交易失败
     */
    FAILED("交易失败"),

    /**
     * 订单已创建
     */
    CREATED("订单已创建"),

    /**
     * 订单已取消
     */
    CANCELED("订单已取消"),

    /**
     * 等待支付
     */
    WAITING_PAYMENT("等待支付");

    /** 描述 */
    private String desc;

    private TradeStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @param status
     * @return
     */
    public static TradeStatusEnum acquireByStatusEnum(String status) {
        Optional<TradeStatusEnum> tradeStatusEnum =
                Arrays.stream(TradeStatusEnum.values())
                        .filter(v -> Objects.equals(v.getDesc(), status))
                        .findFirst();
        return tradeStatusEnum.orElse(TradeStatusEnum.FAILED);
    }

}
