package com.ucsmy.eaccount.config.quartz.util;

public enum ScheduleStatus {

    STOP("0", "停用"), START("1", "启用");

    private String value;
    private String description;

    private ScheduleStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


}
