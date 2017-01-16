package com.cn;


public class Test {
	public static void main(String[] args) {
		String sb = new String("abc");
		System.out.println(sb);
		foo(sb);
		System.out.println(sb);
	}
	public static void foo(String sb){
		sb = "aaaa";
	}
}
