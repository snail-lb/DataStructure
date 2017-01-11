package com.cn;

public class Stack<E> {
	/**栈长度**/
	public int size = 0;
	/**栈顶**/
	public Node<E> stackTop;
	
	class Node<E>{
		/**节点数据**/
		E date;
		/**下一节点**/
		Node<E> next;
		Node(E date,Node<E> next){
			this.date = date;
			this.next = next;
		}
	}
	
	/**
	 * 把项压入堆栈顶部。
	 * @param e
	 */
	public void push(E e){
		if(this.stackTop == null){
			Node<E> node = new Node<E>(e, null);
			this.stackTop = node;
		}else{
			Node<E> node = new Node<E>(e, stackTop);
			this.stackTop = node;
		}
		this.size++;
	}
	
	
	/**
	 * 移除堆栈顶部的对象，并作为此函数的值返回该对象。
	 * 
	 */
	public E pop(){
		E e = this.stackTop.date;
		this.stackTop = this.stackTop.next;
		this.size--;
		return e;
	}
	
	/**
	 * 查看堆栈顶部的对象，但不从堆栈中移除它。
	 * @return
	 */
	public E peek(){
		return this.stackTop.date;
	}
	
	/**
	 * 测试堆栈是否为空。
	 * @return
	 */
	public boolean empty(){
		if(this.stackTop == null && this.size == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 返回对象在堆栈中的位置，以 1 为基数。
	 * @param o
	 * @return 对象到堆栈顶部的位置，以 1 为基数；返回值 -1 表示此对象不在堆栈中。
	 */
	public int search(Object o){
		int num = 1;
		Node<E> node = this.stackTop;
		while(num <= this.size){
			if(o.equals(node.date)){
				return num;
			}
			node = node.next;
			num++;
		}
		return -1;
	}
	
	/**
	 * 遍历输出栈
	 */
	public void traverseStack(){
		Node<E> node = this.stackTop;
		StringBuffer sb = new StringBuffer();
		sb.append("size:" + this.size + " ");
		while (node != null) {
			sb.append(node.date + "-->");
			node = node.next;
		}
		if (this.size == 0) {
			System.out.println(sb.toString());
		} else {
			String str = sb.substring(0, sb.length() - 3);
			System.out.println(str);
		}
	}
}
