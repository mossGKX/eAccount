package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;

@Data
public class EcUserAccountRel extends BaseNode {
    private String accountType;// 账户类型
    private String accountNo;// 账户编号
    private String userNo;// 用户编号
    
}