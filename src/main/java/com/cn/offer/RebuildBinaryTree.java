package com.cn.offer;

/**
 * 重建二叉树
 * 根据一棵树的前序遍历与中序遍历构造二叉树。
 * 注意:
 * 你可以假设树中没有重复的元素。
 * <p>
 * 例如，给出
 * <p>
 * 前序遍历 preorder = [3,9,20,15,7]
 * 中序遍历 inorder = [9,3,15,20,7]
 * <p>
 * 返回如下的二叉树：
 * <p>
 *   3
 *  / \
 * 9   20
 *   /  \
 *  15   7
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-11-12 23:01
 */
public class RebuildBinaryTree {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return buildTreeAssist(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    /**
     * 基本思路：前序遍历的第一个节点是根节点，然后从中序遍历中找到第一个根节点的位置，左边的就是根节点左子树的元素，右边就是根节点右子树的元素
     * 以此为基础使用递归即可解决
     *
     * @param preorder
     * @param pStart
     * @param pEnd
     * @param inorder
     * @param iStart
     * @param iEnd
     * @return
     */
    public TreeNode buildTreeAssist(int[] preorder, int pStart, int pEnd, int[] inorder, int iStart, int iEnd) {
        if (pStart > pEnd) {
            return null;
        }
        if (iStart > iEnd) {
            return null;
        }

        int rootValue = preorder[pStart];
        /*左子树的大小*/
        int leftSize = 0;
        /*这里每次都要遍历，可以考虑将数据存到hashmap中，避免每次找*/
        for (int i = iStart; i <= iEnd; i++) {
            if (inorder[i] == rootValue) {
                break;
            }
            leftSize++;
        }
        /*创建根节点*/
        TreeNode threeNode = new TreeNode(rootValue);
        /*重建左子树*/
        TreeNode left = buildTreeAssist(preorder, pStart + 1, pStart + leftSize, inorder, iStart, iStart + leftSize - 1);
        /*重建右子树*/
        TreeNode right = buildTreeAssist(preorder, pStart + leftSize + 1, pEnd, inorder, iStart + leftSize + 1, iEnd);
        threeNode.left = left;
        threeNode.right = right;
        return threeNode;
    }


    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

    }
}
