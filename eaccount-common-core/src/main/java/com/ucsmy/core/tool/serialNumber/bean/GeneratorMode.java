package com.ucsmy.core.tool.serialNumber.bean;

/**
 * 序列号生成模式
 * Created by ucs on 2017/5/18.
 */
public enum GeneratorMode {

    DEFAULT,
    DB,
    REDIS;

    GeneratorMode() {
    }
}