package com.cn;

import java.util.Collection;

public class LinkList<E> {
	public Node<E> head;// 头结点
	public Node<E> tail;// 尾节点
	public int size = 0;// 链表长度

	public LinkList() {
	}

	public LinkList(Collection<E> c) {
		this();
		addAll(c);
	}

	class Node<E> {
		/**
		 * 节点数据域
		 */
		E date;
		/**
		 * 此节点的下一个节点
		 */
		Node<E> next;
		/**
		 * 此节点的上一个节点
		 */
		Node<E> last;
		Node(Node<E> last, E date, Node<E> next) {
			this.date = date;
			this.next = next;
			this.last = last;
		}
	}

	/**
	 * 将指定元素添加到此列表的结尾。
	 * 
	 * @param date
	 */
	public void add(E date) {
		if (this.head == null && this.tail == null) {
			Node<E> node = new Node<E>(null,date,null);
			this.head = node;
			this.tail = node;
		} else {
			Node<E> node = new Node<E>(this.tail,date,null);
			this.tail.next = node;
			this.tail = node;
		}
		this.size++;
	}

	/**
	 * 在此列表中指定的位置插入指定的元素，其后元素依次向后移动一个单位。
	 * 
	 * @param index
	 *            要在其中插入指定元素的索引
	 * @param date
	 *            要插入的元素
	 * @throws Exception
	 */
	public void add(int index, E date) throws Exception {
		checkPositionIndex(index);
		Node<E> node = getNodeByIndex(index);
		linkBefore(date, node);
	}
	
	/**
	 * 在指定节点之后插入一个节点元素
	 * @param e 新节点元素
	 * @param node 插入位置的节点
	 */
	void linkLast(E e,Node<E> node) {
		Node<E> newNode = null; 
		if(node == this.tail){
			newNode = new Node<E>(node, e, null);
			node.last = newNode;
			this.tail = newNode;
		}else{
			newNode = new Node<E>(node, e, node.next);
			node.next.last = newNode;
			node.next = newNode;
		}
		this.size++;
	}
	
	/**
	 * 在指定节点之前插入一个元素节点
	 * @param e
	 * @param node
	 */
	void linkBefore(E e, Node<E> node) {
		Node<E> newNode = null; 
		if(node == this.head){
			newNode = new Node<E>(null, e, node);
			node.last = newNode;
			this.head = newNode;
		}else{
			newNode = new Node<E>(node.last, e, node);
			node.last.next = newNode;
			node.last = newNode;
		}
		this.size++;
	}
	
	/**
	 * 移除指定节点
	 * @param node
	 */
	void remove(Node<E> node){
		if(node == this.head){
			node.next.last = null;
			this.head = node.next;
			node = null;
		}else if(node == this.tail){
			node.last.next = null;
			this.tail = node.last;
			node = null;
		}else{
			node.last.next = node.next;
			node.next.last = node.last;
			node = null;
		}
		this.size--;
	}
	
	/**
	 * 获取指定索引位置的节点。 链表索引从0开始，即头结点的索引为0，尾节点为size-1
	 * @param index
	 * @return
	 * @throws Exception 
	 */
	Node<E> getNodeByIndex(int index) throws Exception{
		checkPositionIndex(index);
		if(index == 0){
			return this.head;
		}else if(index == (size-1)){
			return this.tail;
		}else{
			Node<E> node = this.head;
			for (int i = 0; i < index; i++) {
				node = node.next;
			}
			return node;
		}
	}

	/**
	 * 移除此列表中指定位置处的元素,并返回此处元素数据
	 * 
	 * @param index
	 * @return 指定位置元素
	 * @throws Exception
	 */
	public E remove(int index) throws Exception {
		Node<E> node = getNodeByIndex(index);
		E e = node.date;
		remove(node);
		return e;
	}

	/**
	 * 添加指定 collection 中的所有元素到此列表的结尾，顺序是指定 collection 的迭代器返回这些元素的顺序。
	 * 
	 * @param c
	 */
	public void addAll(Collection<? extends E> c) {
		@SuppressWarnings("unchecked")
		E[] o = (E[]) c.toArray();
		if (this.head == null && this.tail == null) {
			this.head = new Node<E>(null, (E) o[0], null);
			this.size++;
			this.tail = this.head;
			for(int i = 1; i < o.length; i++){
				add((E)o[i]);
			}
		}else{
			for(int i = 0 ; i < o.length; i++){
				add((E)o[i]);
			}
		}
	}

	/**
	 * 检查位置索引
	 * 
	 * @param index
	 * @throws Exception
	 */
	public void checkPositionIndex(int index) throws Exception {
		if (index < 0 || index >= size) {
			throw new Exception("索引位置越界");
		}
	}
	
	/**
	 * 从此列表中移除所有元素。
	 */
	public void clear(){
		this.head = null;
		this.tail = null;
		this.size = 0;
	}
	
	/**
	 * 遍历链表输出
	 */
	public void traverseLinkList(){
		Node<E> node = this.head;
		StringBuffer sb = new StringBuffer();
		sb.append("size:" + this.size + " ");
		while(node != null){
			sb.append(node.date + "<-->");
			node = node.next;
		}
		if(this.size == 0){
			System.out.println(sb.toString());
		}else{
			String str = sb.substring(0, sb.length()-4);
			System.out.println(str);
		}
	}
}
