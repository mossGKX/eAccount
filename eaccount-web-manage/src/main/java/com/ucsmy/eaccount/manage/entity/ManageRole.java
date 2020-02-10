package com.ucsmy.eaccount.manage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ucsmy.core.bean.BaseNode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author 
 */
@Data
public class ManageRole extends BaseNode {
    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 36, message = "名称最大长度为36")
    private String name;
    /**
     * 描述
     */
    @Length(max = 255, message = "描述最大长度为255")
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ManageMenu> menus;
}