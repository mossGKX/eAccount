package com.ucsmy.core.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 一般树节点，不带子节点
 * Created by ucs_gaokx on 2017/5/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseNode implements Serializable {

    private static final long serialVersionUID = 2L;

    /*
     * 节点编号
	 */
    @JsonProperty("key")
    private String id;
}
