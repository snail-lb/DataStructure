package com.cn;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LinkListTest {
	@Test
	public void testAdd(){
		System.out.print("testAdd(E e):  ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		list.traverseLinkList();
	}
	
	@Test
	public void testAdd2(){
		System.out.print("testAdd(int index,E e): ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		try {
			list.add(0,"李小龙");
			list.add(1,"岳飞");
			list.add(2,"霍元甲");
			list.add(3,"黄飞鸿");
		} catch (Exception e) {
			e.printStackTrace();
		}
		list.traverseLinkList();
	}
	
	@Test
	public void testAddAll(){
		System.out.print("testAddAll(): ");
		List<String> list = new ArrayList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		LinkList<String> linkList = new LinkList<String>(list);
		linkList.traverseLinkList();
	}
	
	@Test
	public void testLinkLast(){
		System.out.print("testLinkLast(E e,Node node): ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		list.linkLast("文天祥1", list.head.next.next);
		list.traverseLinkList();
	}
	
	@Test
	public void testLinkLBefore(){
		System.out.print("testLinkLBefore(E e,Node node): ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		list.linkBefore("文天祥1", list.head.next.next);
		list.traverseLinkList();
	}
	
	@Test
	public void testRemove(){
		System.out.print("testRemove(Node node): ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		list.traverseLinkList();
		list.remove(list.head);
		list.traverseLinkList();
	}
	
	@Test
	public void testRemove2() throws Exception{
		System.out.print("testRemove(int index): ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		list.traverseLinkList();
		list.remove(3);
		list.traverseLinkList();
	}
	
	@Test
	public void testClear() throws Exception{
		System.out.print("testClear(): ");
		LinkList<String> list = new LinkList<String>();
		list.add("黄飞鸿");
		list.add("霍元甲");
		list.add("岳飞");
		list.add("李小龙");
		list.traverseLinkList();
		list.clear();
		list.traverseLinkList();
	}
	
	
}
