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
public class ManageUserProfile extends BaseNode {
    /**
     * 用户姓名
     */
    @NotEmpty(message = "请输入用户姓名")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,4}$", message = "用户姓名只允许中文，且长度为2-4")
    private String name;
    /**
     * 个人介绍
     */
    private String introduce;
    /**
     * 工作电话
     */
    private String telephone;
    /**
     * 手机
     */
    @Pattern(regexp = CheckoutUtils.REGEX_PHONE, message = "手机格式不正确")
    private String mobilephone;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 邮箱
     */
    @Length(max = 32, message = "邮箱长度最大为32")
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 性别
     */
    private String gender;
    /**
     * 员工号
     */
    private String starffId;
    /**
     * 职位
     */
    private String position;
    /**
     * 用户类型，import,ldap，register
     */
    private String type;
    private ManageUserAccount account;
    private ManageOrganization org;
}