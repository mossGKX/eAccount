package com.ucsmy.eaccount.manage.entity;

import com.ucsmy.core.bean.TreeNode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 
 */
@Data
public class ManageOrganization extends TreeNode {
    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 64, message = "名称最大长度为64")
    private String name;

    /**
     * 描述
     */
    @Length(max = 256, message = "描述最大长度为256")
    private String description;

    /**
     * 根机构id
     */
    private String rootId;
}