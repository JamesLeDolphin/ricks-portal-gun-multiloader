package com.jdolphin.ricksportalgun.common.util;


import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    protected TreeNode<T> parent;
    protected T data;
    protected List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.parent = null;
        this.data = data;
        this.children = new ArrayList<>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }
}
