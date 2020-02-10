package com.ucsmy.eaccount.manage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ucsmy.core.bean.TreeNode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class ManageMenu extends TreeNode<ManageMenu> {
    @NotEmpty(message = "菜单文本不能为空")
    @Length(min = 2, max = 50, message = "菜单文本长度为2-50")
    private String label;
    @Length(max = 50, message = "菜单图标最大长度为50")
    private String icon;
    @Length(max = 255, message = "菜单图标最大长度为255")
    private String path;
    @Length(max = 255, message = "权限标识最大长度为255")
    private String sn;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ManageResources> resources;
}
