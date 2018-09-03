package com.cn.datastructure;

import com.cn.datastructure.OrderedListFind;

public class OrderedListFindTest {
	public static void main(String[] args) {
		int[] array = {1,3,5,7,9,24,45,56,67,89};
		int key = 89;
		System.out.println("++++++要查找的数为："+key);
		
		OrderedListFind olf = new OrderedListFind();
        System.out.println("\n顺序查找结果:" + olf.SequentialSearch(array,key));
        System.out.println("折半查找查找结果:" + olf.BinarySearch(array, key));
        System.out.println("插值查找查找结果:" + olf.InterpolationSearch(array, key));
        System.out.println("斐波那契查找结果:" + olf.FibonacciSearch(array, key));
	}
}
