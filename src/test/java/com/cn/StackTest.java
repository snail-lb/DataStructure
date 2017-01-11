package com.cn;

import org.junit.Test;

public class StackTest {
	@Test
	public void testPush(){
		System.out.print("testPush: ");
		Stack<String> stack = new Stack<>();
		stack.push("张三");
		stack.push("李四");
		stack.push("王五");
		stack.traverseStack();
	}
	
	public Stack<String> newStack(){
		Stack<String> stack = new Stack<>();
		stack.push("张三");
		stack.push("李四");
		stack.push("王五");
		return stack;
	}
	
	@Test
	public void testPop(){
		Stack<String> stack = newStack();
		String value = stack.pop();
		System.out.print("testPop:  移除的值为:" + value + "  ");
		stack.traverseStack();
	}
	
	@Test
	public void testPeek(){
		Stack<String> stack = newStack();
		String value = stack.peek();
		System.out.print("testPeek:  获取的值为:" + value + "  ");
		stack.traverseStack();
	}
	
	@Test
	public void testEmpty(){
		Stack<String> stack = newStack();
		Stack<String> stackNull = new Stack<String>();
		System.out.println("testEmpty:  测试是否为空：" + stack.empty() + "   " + stackNull.empty());
	}
	
	@Test
	public void testSearch(){
		Stack<String> stack = newStack();
		stack.traverseStack();
		System.out.println("TestSearch:  查找位置索引:" + stack.search("张三") + stack.search("aa"));
		
	}
	
	
}
