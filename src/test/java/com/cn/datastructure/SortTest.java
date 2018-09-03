package com.cn.datastructure;

import org.junit.Test;

import com.cn.datastructure.Sort;

public class SortTest {
	@Test
	public void bubbleSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.bubbleSort(array);
		System.out.print("冒泡排序： ");
		s.display(array);
	}
	
	@Test
	public void selectSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.selectSort(array);
		System.out.print("选择排序： ");
		s.display(array);
	}
	
	@Test
	public void insertSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.insertSort(array);
		System.out.print("插入排序： ");
		s.display(array);
	}
	
	@Test
	public void shellSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.shellSort(array);
		System.out.print("希尔排序： ");
		s.display(array);
	}
	
	@Test
	public void headSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.headSort(array);
		System.out.print("_堆排序： ");
		s.display(array);
	}
	
	@Test
	public void mergeSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.mergeSort(array);
		System.out.print("归并排序： ");
		s.display(array);
	}
	@Test
	public void quickSortTest(){
		Sort s = new Sort();
		int[] array = {2,5,1,6,2,-1,46,21,42};
		s.quickSort(array);
		System.out.print("快速排序： ");
		s.display(array);
	}

}
