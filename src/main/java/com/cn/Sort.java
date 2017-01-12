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
		//将整个数组构建成一个大顶堆
		for(int i = (array.length-1)/2; i >= 0; i--){
			headBig(array,i,array.length-1);
		}
		//将大顶堆树根上的数据与数组末尾的交换，再将数组除开最后一个再构成一个大顶堆，一次循环知道完成排序
		for(int i = array.length-1; i > 0; i--){
			swap(array,0,i);
			headBig(array,0,i-1);
		}
	}

	/**
	 * 将array调整为一个大顶堆
	 * @param array
	 * @param i
	 * @param j
	 */
	private void headBig(int[] array, int start, int stop) {
		int i;
		int var = array[start];
		for(i = 2*start; i <= stop; i*=2){
			if(i+1 >= array.length){
				if(array[i/2] < array[i]){
					array[i/2] = array[i];
					start = i;
					break;
				}else{
					break;
				}
			}
			if(array[i] < array[i+1] && i < stop){
				++i;
			}
			if(var >= array[i]){
				break;
			}
			array[start] = array[i];
			start = i;
		}
		array[start] = var;
	}
	
	/**
	 * 归并排序
	 * @param array
	 */
	public void mergeSort(int[] array){
		int[] temp = new int[array.length];
		mSort(array, temp, 0, array.length-1);

	}
	//msort函数是将list数组进行分割，merge函数是将分割后的list数组进行排序在组合
    private void mSort(int[] list, int[] temp, int left, int right){
        if(left  == right){
            return;   
        }else{
            int mid = (left + right) / 2;
            mSort(list,temp,left,mid);
            mSort(list,temp,mid+1,right);
            merge(list,temp,left,mid+1,right);
        }
    }
    private void merge(int[] list,int[] temp,int left,int mid,int right){
        int j = 0;
        int leftTemp = left;
        int midTemp = mid - 1;
        int n = right - left + 1;  //当前temp数组中数的个数
        //左右数组第一个数进行比较  把较小的数如到temp数组中
        while(left <= midTemp &&  mid <= right){   
            if(list[left] < list[mid]){
                temp[j++] = list[left++];
            }else{
                temp[j++] = list[mid++];
            }
        }
        //如果左边的数比右边的数多，就将剩下的入到temp数组中    j
        while(left <= midTemp){    
            temp[j++] = list[left++];
        }
        //如果右边打数比左边的数多，就将右边剩下的数加入到temp数组当中去
        while(mid <= right){    
            temp[j++] = list[mid++];
        }
        //将得到的temp数组加到list数组中    
        for(j = 0; j < n; j++){ 
            list[leftTemp+j] = temp[j];
        }         
    }
    
    
    /**
     * 快速排序
     * @param array
     */
    public void quickSort(int[] array){
    	qSort(array, 0, array.length-1);
    }
	private void qSort(int[] array, int left, int right) {
		if(left < right){
			int mid = partition(array, left, right);
			qSort(array, left, mid-1);
			qSort(array, mid+1, right);
		}
	}
	//返回一个关键字，使得这个关键字左边的数都比他小，右边的数都比它大
	private int partition(int[] array, int left, int right) {
		int result =  array[left];
		while(left < right){
			while(left < right && array[right] >= result){
				right--;
			}
			swap(array, left, right);
			while(left < right && array[left] <= result){
				left++;
			}
			swap(array, left, right);
		}
		array[left] = result;
		return left;
	}
}



















