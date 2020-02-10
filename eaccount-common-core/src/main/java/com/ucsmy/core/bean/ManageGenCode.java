package com.ucsmy.core.bean;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class ManageGenCode extends BaseNode {
    @NotEmpty(message = "包名不能为空")
    private String packageName;
    @NotEmpty(message = "实体名不能为空")
    private String entityName;
    @NotEmpty(message = "uri不能为空")
    private String uri;
    private String dataTable;
    private boolean noGen;
    private List<ManageGenAttr> attrs;
}
