package com.cn.datastructure;

import java.util.Arrays;

import org.junit.Test;

import com.cn.datastructure.BinaryTree;

public class BinaryTreeTest {
	@Test
	public void testPreorderTraversal(){
		String[] strArray= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
//		String[] strArray= {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		BinaryTree<String> bt = new BinaryTree<String>(strArray);
		
		String [] pre1 = bt.preorderTraversal(bt.rootNode);
		System.out.println("前序遍历：" + Arrays.toString(pre1));
		
		String [] pre2 = bt.preorderTraversalNoRecursion(bt.rootNode);
		System.out.println("前序遍历(非递归实现)：" + Arrays.toString(pre2));
		
		String [] in1 = bt.inorderTraversal(bt.rootNode);
		System.out.println("中序遍历：" + Arrays.toString(in1));
		
		String [] in2 = bt.inorderTraversalNoRecursion(bt.rootNode);
		System.out.println("中序遍历(非递归实现)：" + Arrays.toString(in2));
		
		String [] post1 = bt.postorderTraversal(bt.rootNode);
		System.out.println("后序遍历：" + Arrays.toString(post1));
		
		String [] post2 = bt.postorderTraversalNoRecursion(bt.rootNode);
		System.out.println("后序遍历(非递归实现)：" + Arrays.toString(post2));
		
		Object[] depth =  bt.depthTraversing(bt.rootNode);
		System.out.println("深度优先遍历：" + Arrays.toString(depth));
		
		Object[] layer =  bt.layerTraversing(bt.rootNode);
		System.out.println("分层遍历：" + Arrays.toString(layer));
		
		System.out.println("二叉树节点数：" + bt.getNodeNumber(bt.rootNode));
		
		System.out.println("二叉树深度：" + bt.getDepth(bt.rootNode));
		
		System.out.println("第k层节点数：" + bt.getNodeNumberInLay(bt.rootNode, 4));
		
		System.out.println("叶子节点数：" + bt.getNodeNumberLeaf(bt.rootNode));
		
		System.out.println("判断两个树结构是否相同：" + bt.isStructureCmp(bt.rootNode,bt.rootNode) + 
				"  " + bt.isStructureCmp(bt.rootNode,bt.rootNode.left));
		
		System.out.println("判断二叉树是否是平衡二叉树：" + bt.isAVL(bt.rootNode) + 
				"  " + bt.isAVL(bt.rootNode.left));
		
		bt.rootNode.right.right = null;
		
		System.out.println("判断二叉树是否是完全二叉树：" + bt.isCompleteBinaryTree(bt.rootNode) + 
				"  " + bt.isCompleteBinaryTree(bt.rootNode.left));
		
		String[] mypre = {"A", "B", "D", "H", "I", "E", "J", "C", "F", "G"};
		String[] myin = {"H", "D", "I", "B", "J", "E", "A", "F", "C", "G"};
		BinaryTree<String> mybt = bt.reBuildBinaryTree(mypre, myin);
		Object[] myobj =  mybt.layerTraversing(mybt.rootNode);
		System.out.println("重建二叉树后进行的分层遍历：" + Arrays.toString(myobj));
		
	}
}
