package com.ucsmy.core.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author  gaokx
 */
@Data
public class ManageLogInfo extends BaseNode {
    private Date createTime;

    private String ipAddress;

    private String logLevel;

    private String message;

    private String userName;

    private String userId;

}