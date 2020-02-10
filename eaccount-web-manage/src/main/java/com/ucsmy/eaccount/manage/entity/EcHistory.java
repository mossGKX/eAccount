package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;

@Data
public class EcHistory extends BaseNode {
    
    private String accountNo;// 账户编号
    private String userNo;// 用户编号
    private Short amount;// 金额
    private Integer riskDay;// 风险预存期
    private java.util.Date editTime;// 修改时间
    private String isAllowSett;// 是否允许结算
    private String remark;// 备注
    private String trxType;// 业务类型
    private String requestNo;// 请求号
    private String bankTrxNo;// 银行流水号
    private String fundDirection;// 资金变动方向
    private java.util.Date createTime;// 创建时间
    private Short balance;// 账户余额
    private String isCompleteSett;// 是否完成结算
    private Long version;// 版本号
}