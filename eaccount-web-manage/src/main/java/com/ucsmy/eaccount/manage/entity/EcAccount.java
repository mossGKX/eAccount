package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;

@Data
public class EcAccount extends BaseNode {
    private Short totalExpend;// 总支出
    
    private Short todayExpend;// 今日支出
    private Long version;// 版本号
    private Short settAmount;// 可结算金额
    private Short securityMoney;// 保证金
    private Short totalIncome;// 总收益
    private String accountType;// 账户类型
    private Short todayIncome;// 今日收益
    private java.util.Date editTime;// 修改时间
    private String remark;// 备注
    private Short creditLine;// 授信额度
    private String status;// 状态
    private String userNo;// 用户编号
    private String accountNo;// 账户编号
    private Short unbalance;// 不可用余额
    private java.util.Date createTime;// 创建时间
    private Short balance;// 账户余额
}