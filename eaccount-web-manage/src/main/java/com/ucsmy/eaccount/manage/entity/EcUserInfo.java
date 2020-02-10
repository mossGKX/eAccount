package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
public class EcUserInfo extends BaseNode {
    @NotEmpty(message = "用户类型不能为空")
    private String userType;// 用户类型

    private String salt;// 加盐

    @NotEmpty(message = "支付密码不能为空")
    private String payPwd;// 支付密码

    private String status;// 状态

    @NotEmpty(message = "登录名不能为空")
    private String loginName;// 登录名

    @NotEmpty(message = "用户姓名不能为空")
    private String userName;// 用户姓名

    private String accountNo;// 账户编号

    @NotEmpty(message = "登录密码不能为空")
    private String password;// 登录密码

    private String userNo;// 用户编号

    private java.util.Date createTime;// 创建时间
}