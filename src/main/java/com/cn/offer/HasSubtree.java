package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-03-31 21:54
 * 输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）
 */
public class HasSubtree {

    public static void main(String[] args) {

    }

    /**
     * 思路： 1. 首先以root1根节点为起点，与root2根节点判断是否是子结构，是子结构返回true结束，
     * 不是子结构，以root1，左右节点一次为起点判断，知道root1节点全部比较一次找到为止
     * @param root1
     * @param root2
     * @return
     */
    public boolean hasSubtree(TreeNode root1,TreeNode root2) {
        if(root1 == null || root2 == null) {
            return false;
        }
        return isSubtree(root1, root2) || hasSubtree(root1.left, root2) || hasSubtree(root1.right, root2);
    }

    public boolean isSubtree(TreeNode root1, TreeNode root2) {
        if(root2 == null) {
            return true;
        }
        if(root1 == null) {
            return false;
        }
        if(root1.val == root2.val) {
            return isSubtree(root1.left,root2.left) && isSubtree(root1.right, root2.right);
        } else {
            return false;
        }
    }

    class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}
