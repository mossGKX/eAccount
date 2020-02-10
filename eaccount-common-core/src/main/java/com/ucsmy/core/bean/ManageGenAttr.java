package com.ucsmy.core.bean;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManageGenAttr extends BaseNode {
    private String jdbcName;
    private String javaName;
    private String des;
    private int jdbcType;
    private String javaType;
    private String insert;
    private String update;
    private String list;
    private String query;
    private int weight;
    private String codeId;

    @Builder
    public ManageGenAttr(String id, String jdbcName, String javaName, String des, int jdbcType, String javaType, String insert, String update, String list, String query, int weight, String codeId) {
        super(id);
        this.jdbcName = jdbcName;
        this.javaName = javaName;
        this.des = des;
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        this.insert = insert;
        this.update = update;
        this.list = list;
        this.query = query;
        this.weight = weight;
        this.codeId = codeId;
    }
}
