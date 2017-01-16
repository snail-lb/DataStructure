package com.cn;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BinaryTree<E> {
	/** 根节点 **/
	Node<E> rootNode;
	/** 树深度 **/
	int depth;

	class Node<E> {
		/** 节点数据 **/
		E date;
		/** 左子树 **/
		Node<E> left;
		/** 右子树 **/
		Node<E> right;

		Node(E date) {
			this.date = date;
			this.left = null;
			this.right = null;
		}

		Node(E date, Node<E> left, Node<E> right) {
			this.date = date;
			this.left = left;
			this.right = right;
		}
	}

	public BinaryTree() {
	}

	/**
	 * 使用数组构造一个完全二叉树
	 * 
	 * @param o
	 */
	public BinaryTree(E[] o) {
		this();
		Node<E> root = null;
		this.rootNode = createFullBinaryTree(root, o, 0);
	}

	/**
	 * 创建满二叉树函数
	 * 
	 * @param root
	 * @param o
	 * @param index
	 * @return
	 */
	private Node<E> createFullBinaryTree(Node<E> root, E[] o, int index) {
		if (index >= o.length) {
			return null;
		}
		root =  new Node<E>(o[index]);
		// 数组是从0开始的，所以左节点的序号为父节点的两倍多一个，右节点为父节点的两倍多两个
		root.left = createFullBinaryTree(root.left, o, 2 * index + 1);
		root.right = createFullBinaryTree(root.right, o, 2 * index + 2);
		return root;
	}

	/**
	 * 前序遍历输出数组
	 * 
	 * @return E[]
	 */
	public E[] preorderTraversal(Node<E> node) {
		List<E> list = new ArrayList<E>();
		preorderTraversalRealize(node,list);
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		return list.toArray(e);
	}

	/**
	 * 前序遍历输出实现
	 * 
	 * @param node
	 */
	private void preorderTraversalRealize(Node<E> node,List<E> list) {
		if (node != null) {
			list.add(node.date);
			preorderTraversalRealize(node.left,list);
			preorderTraversalRealize(node.right,list);
		}
	}
	
	/**
	 * 前序遍历输出数组(非递归实现)
	 * 
	 * @return E[]
	 */
	public E[] preorderTraversalNoRecursion(Node<E> node) {
		if(node == null)
			return null;
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		Stack<Node<E>> stack = new Stack<Node<E>>();
		List<E> list = new ArrayList<E>();
		stack.push(node);
		while(!stack.empty()){
			node = stack.pop();
			list.add(node.date);
			if(node.right != null){
				stack.push(node.right);
			}
			if(node.left != null){
				stack.push(node.left);
			}
		}
		return list.toArray(e);
	}
	

	/**
	 * 中序遍历输出数组
	 * 
	 * @return E[]
	 */
	public E[] inorderTraversal(Node<E> node) {
		List<E> list = new ArrayList<E>();
		inorderTraversalRealize(node,list);
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		return list.toArray(e);
	}

	/**
	 * 中序遍历输出实现
	 * 
	 * @param node
	 */
	private void inorderTraversalRealize(Node<E> node,List<E> list) {
		if (node != null) {
			inorderTraversalRealize(node.left,list);
			list.add(node.date);
			inorderTraversalRealize(node.right,list);
		}
	}
	
	/**
	 * 中序遍历输出，非递归实现
	 * @return
	 */
	public E[] inorderTraversalNoRecursion(Node<E> node){
		if(node == null)
			return null;
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		Stack<Node<E>> stack = new Stack<Node<E>>();
		List<E> list = new ArrayList<E>();
		while(node != null || !stack.empty()){
			//存在左子树时
			while(node != null){
				stack.push(node);
				node = node.left;
			}
			//栈非空时
			if(!stack.empty()){
				node = stack.pop();
				list.add(node.date);
				node = node.right;
			}
		}
		return list.toArray(e);
	}

	/**
	 * 后序遍历输出数组
	 * 
	 * @return E[]
	 */
	public E[] postorderTraversal(Node<E> node) {
		List<E> list = new ArrayList<E>();
		postorderTraversalRealize(node,list);
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		return list.toArray(e);
	}

	/**
	 * 后序遍历输出实现
	 * 
	 * @param node
	 */
	private void postorderTraversalRealize(Node<E> node,List<E> list) {
		if (node != null) {
			postorderTraversalRealize(node.left,list);
			postorderTraversalRealize(node.right,list);
			list.add(node.date);
		}
	}
	
	/**
	 * 后续遍历（非递归输出）
	 * @param node
	 * @return
	 */
	public E[] postorderTraversalNoRecursion(Node<E> node){
		if(node == null)
			return null;
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		Stack<Node<E>> stack = new Stack<Node<E>>();
		List<E> list = new ArrayList<E>();
		Node<E> prv = node; //记录之前遍历的右结点  
		while(node != null || !stack.empty()){
			//存在左子树时
			while(node != null){
				stack.push(node);
				node = node.left;
			}
			//栈非空时
			if(!stack.empty()){
				Node<E> nodeRight = stack.peek().right;
				/*如果右结点为空，或者右结点之前遍历过，获取根结点数据*/  
				if(nodeRight == null || nodeRight == prv ){
					node = stack.pop();
					list.add(node.date);
					prv = node;
					node = null;
				}else{
					node = nodeRight;
				}
			}
		}
		return list.toArray(e);
	}

	/**
	 * 广度优先搜索(分层遍历二叉树): 使用队列实现。队列初始化，将根节点压入队列。当队列不为空，
	 * 进行如下操作：弹出一个节点，访问，若左子节点或右子节点不为空，将其压入队列。
	 * 
	 * @param node
	 * @return
	 */
	public E[] layerTraversing(Node<E> node) {
		List<E> list = new ArrayList<E>();
		layerTraversingRealize(node, list);
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		return list.toArray(e);
	}
	/**
	 * 分层遍历辅助函数
	 * @param node
	 * @param list
	 */
	private void layerTraversingRealize(Node<E> node, List<E> list) {
		Queue<Node<E>> queue = new Queue<Node<E>>();
		queue.add(node);
		while (!queue.empty()) {
			Node<E> n = queue.poll();
			list.add(n.date);
			if (n.left != null) {
				queue.add(n.left);
			}
			if (n.right != null) {
				queue.add(n.right);
			}
		}
	}

	/**
	 * 使用深度优先搜索遍历二叉树,这个结果和前序遍历是一样的
	 * 
	 * @param node
	 * @return
	 */
	public E[] depthTraversing(Node<E> node) {
		List<E> list = new ArrayList<E>();
		depthTraversingRealize(node, list);
		@SuppressWarnings("unchecked")
		E[] e = (E[])Array.newInstance(node.date.getClass(), 0);//使用泛型数组进行记录
		return list.toArray(e);
	}

	private void depthTraversingRealize(Node<E> node, List<E> list) {
		if (node != null) {
			list.add(node.date);
			depthTraversingRealize(node.left, list);
			depthTraversingRealize(node.right, list);
		}
	}

	/**
	 * 计算二叉树节点的个数
	 * 
	 * @param node
	 * @return 二叉树节点的个数
	 */
	public int getNodeNumber(Node<E> node) {
		if (node == null) {
			return 0;
		}
		return getNodeNumber(node.left) + getNodeNumber(node.right) + 1;
	}

	/**
	 * 求二叉树深度
	 * 
	 * @return 二叉树深度
	 */
	public int getDepth(Node<E> node) {
		if (node == null) {
			return 0;
		}
		int leftDepth = getDepth(node.left);
		int rightDepth = getDepth(node.right);
		return leftDepth > rightDepth ? leftDepth + 1 : rightDepth + 1;
	}

	/**
	 * 求二叉树第K层的节点个数 递归解法： （1）如果二叉树为空或者k<1返回0 （2）如果二叉树不为空并且k==1，返回1
	 * （3）如果二叉树不为空且k>1，返回左子树中k-1层的节点个数与右子树k-1层节点个数之和
	 * 
	 * @param k
	 * @return 二叉树第K层的节点个数
	 */
	public int getNodeNumberInLay(Node<E> node, int k) {
		if (node == null || k < 1) {
			return 0;
		}
		if (k == 1) {
			return 1;
		}
		int leftNodeNum = getNodeNumberInLay(node.left, k - 1);
		int rightNodeNum = getNodeNumberInLay(node.right, k - 1);
		return leftNodeNum + rightNodeNum;
	}

	/**
	 * 求二叉树中叶子节点的个数
	 * 
	 * @param node
	 * @return
	 */
	public int getNodeNumberLeaf(Node<E> node) {
		if (node == null) {
			return 0;
		}
		if (node.left == null && node.right == null) {
			return 1;
		}
		int leftNodeNum = getNodeNumberLeaf(node.left);
		int rightNodeNum = getNodeNumberLeaf(node.right);
		return leftNodeNum + rightNodeNum;
	}

	/**
	 * 判断两棵二叉树是否结构相同 不考虑数据内容。结构相同意味着对应的左子树和对应的右子树都结构相同。 递归解法： （1）如果两棵二叉树都为空，返回真
	 * （2）如果两棵二叉树一棵为空，另一棵不为空，返回假 （3）如果两棵二叉树都不为空，如果对应的左子树和右子树都同构返回真，其他返回假
	 * 
	 * @param node1
	 * @param node2
	 * @return
	 */
	public boolean isStructureCmp(Node<E> node1, Node<E> node2) {
		if (node1 == null && node2 == null) {
			return true;
		} else if (node1 == null || node2 == null) {
			return false;
		} else {
			boolean leftCmp = isStructureCmp(node1.left, node2.left);
			boolean rightCmp = isStructureCmp(node1.right, node2.right);
			return leftCmp && rightCmp;
		}
	}

	/**
	 * 判断是否是平衡二叉树
	 * 
	 * @param node
	 * @return
	 */
	public boolean isAVL(Node<E> node) {
		if (node == null) {
			return true;
		}
		int leftHeight = getDepth(node.left);
		int rightHeight = getDepth(node.right);
		if (Math.abs(leftHeight - rightHeight) > 1) {
			return false;
		} else {
			return isAVL(node.left) && isAVL(node.right);
		}
	}

	/**
	 * 判断是否完全二叉树 1.当发现有一个节点的左子树为空，右子树不为空时 直接返回false.
	 * 2.当发现有一个节点的左子树不为空，右子树为空时，置标志位为1。 3.当发现有一个节点的左右子树均为空时，置标志位为1。
	 * 
	 * @param node
	 * @return
	 */
	public boolean isCompleteBinaryTree(Node<E> node) {
		if (node == null) {
			return true;
		}
		Queue<Node<E>> queue = new Queue<Node<E>>();
		queue.add(node);
		int flag = 0;// 标记此节点以下的节点均应为叶子节点（没有左右孩子），否则此树为一棵非完全二叉树。
		while (!queue.empty()) {
			Node<E> n = queue.poll();
			if (n.left != null) {
				if (flag == 1) {
					return false;
				}
				queue.add(n.left);
				if (n.right != null) {
					queue.add(n.right);
				} else {
					flag = 1;
				}
			} else {
				if (n.right != null) {
					return false;
				}
				flag = 1;
			}
		}
		return true;
	}

	/**
	 * 根据前序遍历结果和中序遍历结果重建二叉树
	 * 
	 * @param preorderTraversalArray
	 *            前序遍历结果
	 * @param inorderTraversalArray
	 *            中序便利结果
	 * @return 二叉树
	 */
	public BinaryTree<E> reBuildBinaryTree(E[] preorderTraversalArray, E[] inorderTraversalArray) {
		if (preorderTraversalArray == null || inorderTraversalArray == null) {
			return null;
		}
		Node<E> root = reBuildBinaryTreeRealize(preorderTraversalArray, 0, preorderTraversalArray.length - 1,
				inorderTraversalArray, 0, inorderTraversalArray.length - 1);
		BinaryTree<E> bt = new BinaryTree<E>();
		bt.rootNode = root;
		bt.depth = getDepth(root);
		return bt;
	}
	/**
	 *  前序遍历的第一个节点一定是二叉树的根节点（以a记录），中序遍历以a为分界线，a左边的一定是左子树一边的（记录下左边的个数为x），且为左子树的中序遍历结果，
	 * a右边的一定是右子树一边的（记录下右边的个数为y），且为右子树中序遍历的结果。再把前序遍历结果a后面x个数作为左子树的前序遍历，剩下的y个作为右子树的前序遍历，
	 * 再一次进行递归建立，直到完全建立二叉树
	 * 
	 * @param preOrder 前序遍历
	 * @param startPreIndex 前序遍历起始位置
	 * @param endPreIndex 前序遍历结束为止
	 * @param inOrder	后序遍历
	 * @param startInIndex	后续遍历起始位置
	 * @param endInIndex 后序遍历结束位置
	 * @return
	 */
	public Node<E> reBuildBinaryTreeRealize(E[] preOrder, int startPreIndex, int endPreIndex, E[] inOrder,
			int startInIndex, int endInIndex) {

		Node<E> root = new Node<E>(preOrder[startPreIndex]);
		// 只有一个元素
		if (startPreIndex == endPreIndex) {
			if (startInIndex == endInIndex && preOrder[startPreIndex] == inOrder[startInIndex]) {
				return root;
			} else {
				throw new RuntimeException("出错");
			}
		}

		// 在中序遍历中找到根结点的索引
		int rootInIndex = startInIndex;

		while (rootInIndex <= endInIndex && inOrder[rootInIndex] != preOrder[startPreIndex]) {
			++rootInIndex;
		}

		if (rootInIndex == endInIndex && inOrder[rootInIndex] != preOrder[startPreIndex]) {
			throw new RuntimeException("出错");
		}

		int leftLength = rootInIndex - startInIndex;

		int leftPreOrderEndIndex = startPreIndex + leftLength;

		if (leftLength > 0) {
			// 构建左子树
			root.left = reBuildBinaryTreeRealize(preOrder, startPreIndex + 1, leftPreOrderEndIndex, inOrder, startInIndex,
					rootInIndex - 1);
		}

		if (leftLength < endPreIndex - startPreIndex) {
			// 右子树有元素,构建右子树
			root.right = reBuildBinaryTreeRealize(preOrder, leftPreOrderEndIndex + 1, endPreIndex, inOrder, rootInIndex + 1,
					endInIndex);
		}
		return root;
	}
}
