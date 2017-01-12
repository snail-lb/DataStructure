package com.cn;

import java.util.Arrays;

/**
 * 经典排序复习
 * @author lvbiao
 *
 */
public class Sort {

	
	/**
	 * 交换数组中两个指定位置的数据
	 * @param array
	 * @param i
	 * @param j
	 */
	private void swap(int[] array, int i, int j) {
		int var;
		var = array[i];
		array[i] = array[j];
		array[j] = var;
	}
	
	public void display(int[] array){
		System.out.println(Arrays.toString(array));
	}
	
	/**
	 * 冒泡排序
	 * @param array
	 */
	public void bubbleSort(int[] array){
		for(int i = 0; i < array.length; i++){
			for(int j = array.length-1; j > i; j--){
				if(array[j-1] > array[j]){
					swap(array,j-1,j);
				}
			}
		}
	}
	
	/**
	 * 选择排序
	 * @param array
	 */
	public void selectSort(int[] array){
		int min;
		for(int i = 0; i < array.length; i++){
			min = i;
			for(int j = i+1; j < array.length; j++){
				if(array[min] > array[j]){
					min = j;
				}
			}
			if(min != i){
				swap(array, i, min);
			}
		}
	}
	
	/**
	 * 插入排序
	 * @param array
	 */
	public void insertSort(int[] array){
		int j;
		for(int i = 1; i < array.length; i ++){
			if(array[i] < array[i-1]){
				int var = array[i];
				j = i - 1;
				while(j >= 0 && var < array[j]){
					array[j+1] = array[j];
					j--;
				}
				array[j+1] = var;
			}
		}
	}
	
	/**
	 * 希尔排序
	 * @param array
	 */
	public void shellSort(int[] array){
		int length = array.length;
		int j;
		do{
			length = length/3 + 1;
			for(int i = length; i < array.length; i ++){
				if(array[i] < array[i-length]){
					int var = array[i];
					j = i -length;
					while(j >= 0 && var < array[j]){
						array[j+length] = array[j];
						j-=length;
					}
					array[j+length] = var;
				}
			}
		}while(length > 1);
	}

	
	/**
	 * 推排序
	 * @param array
	 */
	public void headSort(int[] array){
		
	}
	
}
