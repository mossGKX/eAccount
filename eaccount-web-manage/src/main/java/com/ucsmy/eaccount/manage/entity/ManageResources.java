package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ManageResources extends BaseNode {
    @NotEmpty(message = "模块不能为空")
    @Length(max = 255, message = "模块最大长度为255")
    private String groupName;
    @NotEmpty(message = "资源名称不能为空")
    @Length(max = 255, message = "资源名称最大长度为255")
    private String name;
    @NotEmpty(message = "资源地址不能为空")
    @Length(max = 255, message = "资源地址最大长度为255")
    private String url;
}
