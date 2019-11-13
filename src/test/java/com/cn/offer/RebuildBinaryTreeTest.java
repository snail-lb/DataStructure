package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class RebuildBinaryTreeTest {

    @Test
    public void buildTree() {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};
        RebuildBinaryTree tree = new RebuildBinaryTree();
        RebuildBinaryTree.TreeNode treeNode = tree.buildTree(preorder, inorder);
        System.out.println();
    }


}