package com.ucsmy.core.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树的基本节点
 * Created by ucs_gaokx on 2017/4/14.
 */
@Data
@NoArgsConstructor
public class TreeNode<E> extends BaseNode {

    /*
     * 父节点模块编号
     */
    private String pid;

    /*
     * 子节点列表
     */
    private List<E> children;

    private int weight;

    public TreeNode(String id, String pid, List<E> children, int weight) {
        super(id);
        this.pid = pid;
        this.children = children;
        this.weight = weight;
    }
}
