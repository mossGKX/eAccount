package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * @author 
 */
@Data
public class ManageIpScheduleTask extends BaseNode {

    /**
     * 任务名称编码
     */
    @NotEmpty(message = "请输入任务码")
    @Length(max = 100, message = "任务码长度不能超过100")
    @Pattern(regexp = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$", message = "任务码为字母、数字、下划线组合，且不能以下划线开头或结尾")
    private String taskCode;

    /**
     * 任务名
     */
    @NotEmpty(message = "请输入任务名称")
    @Length(max = 100, message = "任务名称长度不能超过100")
    private String taskName;

    /**
     * 任务执行表达式
     */
    @NotEmpty(message = "请输入定时配置")
    @Length(max = 100, message = "定时配置长度不能超过100")
    private String taskConf;

    /**
     * 任务执行类
     */
    @NotEmpty(message = "请输入执行地址")
    @Length(max = 100, message = "执行地址长度不能超过100")
    @Pattern(regexp = "^[a-zA-Z0-9.]+$", message = "执行地址不能为特殊字符或汉字")
    private String taskClass;

    /**
     * 任务执行的服务器
     */
    @NotEmpty(message = "请输入指定IP")
    @Length(max = 100, message = "指定IP长度不能超过100")
    @Pattern(regexp = "^[0-9.]+$", message = "IP格式错误")
    private String taskServerIp;

    /**
     * 任务状态1:启用;0：禁用
     */
    private String status;

    /**
     * 备注
     */
    @NotEmpty(message = "请输入备注")
    @Length(max = 250, message = "备注长度不能超过250")
    private String remark;

}