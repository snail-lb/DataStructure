package com.cn;

import java.util.Arrays;

import org.junit.Test;

public class BinaryTreeTest {
	@Test
	public void testPreorderTraversal(){
//		String[] strArray= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		String[] strArray= {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		BinaryTree<String> bt = new BinaryTree<String>(strArray);
		
		String [] pre = bt.preorderTraversal(bt.rootNode);
		System.out.println("前序遍历：" + Arrays.toString(pre));
		
		String [] in = bt.inorderTraversal(bt.rootNode);
		System.out.println("中序遍历：" + Arrays.toString(in));
		
		String [] post = bt.postorderTraversal(bt.rootNode);
		System.out.println("后序遍历：" + Arrays.toString(post));
	}
}
