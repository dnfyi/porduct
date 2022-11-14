package com.example.springboot2022nocv.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 把没有层级关系的集合变成有层级关系的集合
 */
public class TreeNodeBuilder {
    public static List<TreeNode> build(List<TreeNode> treeNodes, Integer topPid) {
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for (TreeNode n1 : treeNodes) {
            //找到根
            if (n1.getPid()==topPid){
                nodes.add(n1);
            }
            for (TreeNode n2 : treeNodes) {
                //找到子
                if (n1.getId()==n2.getPid()){
                    n1.getChildren().add(n2);
                }
            }
        }
        return nodes;
    }
}
