package com.cn;


public class BinaryTree<E> {
	/**根节点**/
	Node<E> rootNode;
	/**树深度**/
	int depth;
	
	class Node<E>{
		/**节点数据**/
		E date;
		/**左子树**/
		Node<E> left;
		/**左子树**/
		Node<E> right;
		Node(E date){
			this.date = date;
			this.left = null;
			this.right = null;
		}
		Node(E date, Node<E> left, Node<E> right){
			this.date = date;
			this.left = left;
			this.right = right;
		}
	}
	
	public BinaryTree(){}
	
	/**
	 * 使用数组构造一个完全二叉树
	 * @param o
	 */
	public BinaryTree(Object[] o){
		this();
		Node<E> root = null;
		this.rootNode = createFullBinaryTree(root,o,0);
	}
	
	/**
	 * 创建满二叉树函数
	 * @param root
	 * @param o
	 * @param index
	 * @return
	 */
	private Node<E> createFullBinaryTree(Node<E> root, Object[] o, int index){
		if (index >= o.length) {
			return null;
		}
		root = new Node(o[index]);
		//数组是从0开始的，所以左节点的序号为父节点的两倍多一个，右节点为父节点的两倍多两个
		root.left = createFullBinaryTree(root.left, o, 2*index+1);
		root.right = createFullBinaryTree(root.right, o, 2*index+2);
		return root;
	}
	
	//用于存储二叉树遍历后的结果
	StringBuffer sb = new StringBuffer();
	/**
	 * 前序遍历输出数组
	 * @return E[]
	 */
	public E[] preorderTraversal(Node<E> node){
		sb = sb.delete(0, sb.length());
		preorderTraversalRealize(node);
		E[] e= (E[]) sb.toString().split(",;.,");
		sb = sb.delete(0, sb.length());
		return e;
	}
	/**
	 * 前序遍历输出实现
	 * @param node
	 */
	public void  preorderTraversalRealize(Node<E> node){
		if(node != null){
			sb.append(node.date + ",;.,");//这里用几个分隔符进行分割，是为了防止二叉树的数据域中含有相同的字符
			preorderTraversalRealize(node.left);
			preorderTraversalRealize(node.right);
		}
	}
	
	/**
	 * 中序遍历输出数组
	 * @return E[]
	 */
	public E[] inorderTraversal(Node<E> node){
		sb = sb.delete(0, sb.length());
		inorderTraversalRealize(node);
		E[] e= (E[]) sb.toString().split(",;.,");
		sb = sb.delete(0, sb.length());
		return e;
	}
	/**
	 * 中序遍历输出实现
	 * @param node
	 */
	public void  inorderTraversalRealize(Node<E> node){
		if(node != null){
			inorderTraversalRealize(node.left);
			sb.append(node.date + ",;.,");//这里用几个分隔符进行分割，是为了防止二叉树的数据域中含有相同的字符
			inorderTraversalRealize(node.right);
		}
	}
	/**
	 * 后序遍历输出数组
	 * @return E[]
	 */
	public E[] postorderTraversal(Node<E> node){
		sb = sb.delete(0, sb.length());
		postorderTraversalRealize(node);
		E[] e= (E[]) sb.toString().split(",;.,");
		sb = sb.delete(0, sb.length());
		return e;
	}
	/**
	 * 后序遍历输出实现
	 * @param node
	 */
	public void  postorderTraversalRealize(Node<E> node){
		if(node != null){
			postorderTraversalRealize(node.left);
			postorderTraversalRealize(node.right);
			sb.append(node.date + ",;.,");//这里用几个分隔符进行分割，是为了防止二叉树的数据域中含有相同的字符
		}
	}
	
}
