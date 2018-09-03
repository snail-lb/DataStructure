package com.cn.datastructure;

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
	 * 两两比较相邻记录的关键字，如果反序则交换，直到没有反序的记录为止。
	 * 时间复杂度: 最好：O(n²) 最坏：O(n²) 平均：O(n²)
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
	 * 简单选择排序法（Simple Selection Sort）就是通过n-i次关键字间的比较，从n-i+1个记录中选出关键字最小的记录，并和第i(1≤i≤n)个记录交换之。
	 * 时间复杂度: 最好：O(n²) 最坏：O(n²) 平均：O(n²)
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
	 * 直接插入排序（Straight Insertion Sort）的基本操作是将一个记录插入到已经排好序的有序表中，从而得到一个新的、记录数增1的有序表。
	 * 时间复杂度: 最好：O(n) 最坏：O(n²) 平均：O(n²)
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
	 * 我们将原本有大量记录数的记录进行分组。分割成若干个子序列，此时每个子序列待排序的记录个数就比较少了，然后在这些子序列内分别进行直接插入排序，
	 * 当整个序列都基本有序时，注意只是基本有序时，再对全体记录进行一次直接插入排序。
	 * 时间复杂度: 最好：  最坏：  平均： O(n^1.25)
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
	 * 堆排序（Heap Sort）就是利用堆（假设利用大顶堆）进行排序的方法。它的基本思想是，将待排序的序列构造成一个大顶堆。
	 * 此时，整个序列的最大值就是堆顶的根结点。将它与堆数组的末尾元素进行交换，此时末尾元素就是最大值，然后将剩余的n-1个序列重新构造成一个堆，
	 * 这样就会得到n个元素中的次小值。如此反复执行，便能得到一个有序序列了。（大顶堆：在完全二叉树中，每个结点的值都大于或等于其左右孩子结点的值）
	 * 时间复杂度: 最好：O(nLog2n) 最坏：O(nLog2n) 平均：O(nLog2n)
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
	 * 归并排序（Merging Sort）就是利用归并的思想实现的排序方法。它的原理是假设初始序列含有n个记录，则可以看成是n个有序的子序列，
	 * 每个子序列的长度为1，然后两两归 并，得到⌈n/2⌉（⌈x⌉表示不小于x的最小整数）个长度为2的有序子序列；再两两归并，…… 如此重复，
	 * 直至得到一个长度为n的有序序列为止，这 种排序方法称为2路归并排序。
	 * 时间复杂度: 最好：O(nlogn) 最坏：O(nlogn) 平均：O(nlogn)
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
	 * 快速排序（Quick Sort）的基本思想是通过一趟排序将待排记录分割成独立的两部分，其中一部分记录的关键字均比另一部分记录的关键字小，
	 * 则可分别对这两部分记录继续进行排序，以达到整个序列有序的目的。
	 * 时间复杂度: 最好：O(nlogn) 最坏：O(n²) 平均：O(nlogn)
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



















