package com.ucsmy.core.bean;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by cui_fate on 2016/10/7.
 */
@Data
public class MenuTree extends TreeNode<MenuTree> {
    private String label;
    private String icon;
    private String path;

    @Builder
    public MenuTree(String id, String parentId, List<MenuTree> children, int priority, String label, String icon, String path) {
        super(id, parentId, children, priority);
        this.label = label;
        this.icon = icon;
        this.path = path;
    }
}
