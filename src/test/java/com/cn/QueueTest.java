package com.cn;

import org.junit.Test;

public class QueueTest {
	@Test
	public void testAdd(){
		System.out.print("testAdd()");
		Queue<String> queue = new Queue<String>();
		queue.add("张三");
		queue.add("李四");
		queue.add("王五");
		queue.traverseQueue();
	}
	
	public Queue<String> newQueue(){
		Queue<String> queue = new Queue<String>();
		queue.add("张三");
		queue.add("李四");
		queue.add("王五");
		return queue;
	}
	
	@Test
	public void testElement(){
		Queue<String> queue = newQueue();
		System.out.println("testElement(): " + queue.element());
		queue.traverseQueue();
	}
	
	@Test
	public void testPeek(){
		Queue<String> queue = newQueue();
		System.out.println("testPeek(): " + queue.peek());
		queue.traverseQueue();
	}
	
	@Test
	public void testPoll(){
		Queue<String> queue = newQueue();
		System.out.println("testPoll(): " + queue.poll());
		queue.traverseQueue();
	}
	
	@Test
	public void testClear(){
		Queue<String> queue = newQueue();
		queue.clear();
		System.out.print("testClear(): ");
		queue.traverseQueue();
	}
}
