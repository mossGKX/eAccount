package com.ucsmy.core.tool.log.log4j2;

/**
 * log输出类型
 * Created by ucs_gaokx on 2017/4/28.
 */
public enum LogOuputTarget {

    FILE("file", "日志文件输出"),
    DATABASE("database", "数据库输出");

    LogOuputTarget(String name, String des) {

    }
}
