package com.ucsmy.core.utils;

import com.ucsmy.core.bean.TreeNode;

import java.util.*;

/**
 * 树工具类
 * Created by ucs_gaokx on 2017/4/14.
 */
public class TreeUtils {

    /**
     * 将列表封装成树的结构
     *
     * @param list
     * @return
     */
    public static List<? extends TreeNode> getTreeList(List<? extends TreeNode> list) {
        List<TreeNode> nodeList = new ArrayList<>();
        for (TreeNode node1 : list) {
            boolean mark = false;
            for (TreeNode node2 : list) {
                if (node2.getChildren() == null) {
                    node2.setChildren(new ArrayList<TreeNode>());
                }
                if (node1.getPid() != null && node1.getPid().equals(node2.getId())) {
                    mark = true;
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if (!mark && (node1.getPid() == null
                    || "".equals(node1.getPid().trim())
                    || "1".equals(node1.getPid().trim())) ) {
                nodeList.add(node1);
            }
        }
        return nodeList;
    }

    public static <T extends TreeNode> List<T > createTree(List<T> treeData) {
        Set<String> authoritys_set = new TreeSet<>();
        // 以父节点生成MAP
        Map<String, List<T>> map = new HashMap<>();
        List<T> list = new ArrayList<>();
        treeData.forEach(tree -> {
            if(authoritys_set.contains(tree.getId()))
                return;
            authoritys_set.add(tree.getId());

            if (!map.containsKey(tree.getPid())) {
                map.put(tree.getPid(), new ArrayList<>());
            }
            map.get(tree.getPid()).add(tree);
            if(StringUtils.isEmpty(tree.getPid()) || "0".equals(tree.getPid()))
                list.add(tree);
        });
        createTree(list, map);
        return list;
    }

    private static <T extends TreeNode> void createTree(List<T> tree, Map<String, List<T>> map) {
        if(tree == null)
            return;
        // 排序
        tree.sort(Comparator.comparingInt(T::getWeight));
        tree.forEach(p -> {
            if (map.containsKey(p.getPid())) {
                p.setChildren(map.get(p.getId()));
                createTree(map.get(p.getId()), map);
            } else {
                p.getChildren().clear();
            }
        });
    }
}
