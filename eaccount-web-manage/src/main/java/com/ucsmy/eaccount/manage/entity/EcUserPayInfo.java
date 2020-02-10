package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;

@Data
public class EcUserPayInfo extends BaseNode {
    private String userNo;// 用户编号
    private String appType;// APP类型
    private String offlineAppId;// 支付宝线下产品appid
    private String partnerKey;// 商户key
    private String userName;// 用户姓名
    private int version;// 版本号
    private String appSectet;// 支付商户secret
    private String merchantId;// 支付商户ID
    
    private String payWayCode;// 通道编号
    private String payWayName;// 支付通道名称
    private String appId;// 支付appid
    private String status;// 状态
    private String remark;// 备注
    private String rsaPrivateKey;// 支付私钥
    private String rsaPublicKey;// 支付公钥
    private java.util.Date createTime;// 创建时间
    private java.util.Date editTime;// 修改时间
}