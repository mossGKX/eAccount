package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.BaseNode;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ManageConfig extends BaseNode {
    @NotEmpty(message = "参数组不能为空")
    private String groupName;// 参数组
    @NotEmpty(message = "参数名称不能为空")
    private String name;// 参数名称
    @NotEmpty(message = "参数值不能为空")
    private String value;// 参数值
    private String des;// 描述
    private String status;// 状态
}