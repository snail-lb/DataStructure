package com.cn;


public class Queue<E> {
	/**队列长度**/
	public int size = 0;
	/**队首**/
	public Node<E> head;
	/**队尾**/
	public Node<E> tail;
	
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
	 * 将指定的元素插入此队列
	 * @param e
	 */
	public void add(E e){
		if(this.head == null || this.tail == null){
			Node<E> node = new Node<E>(e, null);
			this.head = node;
			this.tail = node;
		}else{
			Node<E> node = new Node<E>(e, null);
			this.tail.next = node;
			this.tail = node;
		}
		this.size++;
	}
	
	
	/**
	 * 获取，但是不移除此队列的头
	 * 
	 */
	public E element(){
		return this.head.date;
	}
	
	/**
	 * 获取但不移除此队列的头；如果此队列为空，则返回 null。
	 * @return
	 */
	public E peek(){
		if(this.head != null){
			return this.head.date;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取并移除此队列的头，如果此队列为空，则返回 null。
	 * @return
	 */
	public E poll(){
		if(this.head != null){
			E e = this.head.date;
			this.head = this.head.next;
			this.size--;
			return e;
		}else{
			return null;
		}
	}
	
	/**
	 * 清空队列
	 */
	public void clear(){
		if(this.head != null){
			this.head = null;
			this.tail = null;
			this.size = 0;
		}
	}
	
	/**
	 * 遍历输出栈
	 */
	public void traverseQueue(){
		Node<E> node = this.head;
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
	
	/**
	 * 判断此队列是否为空
	 * @return
	 */
	public boolean empty(){
		if(this.head == null || this.tail == null || this.size == 0){
			return true;
		}
		return false;
	}
}
