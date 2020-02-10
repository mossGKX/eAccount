package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import com.ucsmy.core.utils.CheckoutUtils;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author 
 */
@Data
public class ManageUserAccount extends BaseNode {
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 密码
     */
    private String password;
    /**
     * 加密盐值
     */
    private String salt;
    /**
     * 0:正常;  1：停用
     */
    private Byte status;
    /**
     * 账号
     */
    @NotEmpty(message = "请输入登录名")
    @Length(min = 3, max = 32, message = "登录名长度为2-32")
    private String account;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 手机
     */
    @Pattern(regexp = CheckoutUtils.REGEX_PHONE, message = "手机格式不正确")
    private String mobilephone;
    private ManageRole role;
}