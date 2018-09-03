package com.cn.datastructure;

/**
 * 有序表查找
 * 
 * @author lvbiao
 *
 */
public class OrderedListFind {

	/**
	 * 顺序优化查找：效率极为底下，但是算法简单，适用于小型数据查找；
	 * @param array
	 * @param key
	 * @return
	 */
	public int SequentialSearch(int[] array, int key){
		if(!checkScope(array, key)){
			return -1;
		}
		for(int i = 0; i < array.length; i++){
			if(array[i] == key){
				return i;
			}
		}
		return -1;
		
	}

	/**
	 * 折半查找：又称为二分查找，它是从查找表的中间开始查找。 查找结果只需要找其中一半的数据记录即可。
	 * 效率较顺序查找提高不少。比较适用与静态表，一次排序后不在变化；
	 * 
	 * @param array
	 * @param key
	 * @return
	 */
	public int BinarySearch(int[] array, int key) {
		if(!checkScope(array, key)){
			return -1;
		}
		int low = 0;
		int high = array.length - 1;
		int mid;
		while(low <= high){
			mid = (low + high)/2;
			if(key > array[mid]){
				low = mid + 1;
			}else if(key < array[mid]){
				high = mid -1;
			}else{
				return mid;
			}
		}
		return -1;
	}

	/**
	 * 插值查找：与折半查找比较相似，只是把中间之mid的公式进行了变换将mid = (low+high)/2;换成了
	 *  mid = low + (high - low) * (key - sum[low]) / (sum[high] - sum[low]);
	 * 插值查找的效率比折半查找的效率又要高出不少，比较适用与表长较大，而关键字又分布得比较均匀的表查找；
	 * 
	 * @param array
	 * @param key
	 * @return
	 */
	public int InterpolationSearch(int[] array, int key) {
		if(!checkScope(array, key)){
			return -1;
		}
		int low = 0;
		int high = array.length - 1;
		int mid;
		while(low < high){
			mid = low + (high - low) * (key - array[low]) / (array[high] - array[low]);
			if(key > array[mid]){
				low = mid + 1;
			}else if(key < array[mid]){
				high = mid -1;
			}else{
				return mid;
			}
		}
		return -1;
	}

	/**
	 * 斐波那契查找：是利用了黄金分割的原理来进行查找，平均性能要由优于折半查找，但是如果是最坏的情况，
	 * 则效率低于折半查找（要查找的关键字一直比较靠近黄金分割较长的那一段），但是运算比较简单，只有最简单的加减运算；
	 * 
	 * @param array
	 * @param key
	 * @return
	 */
	public int FibonacciSearch(int[] array, int key) {
		if(!checkScope(array, key)){
			return -1;
		}
		int[] F = new int[array.length];
		F[0] = 0;
		F[1] = 1;
		int k = 0;//记录数组长度在斐波那契数列中的位置
		for(int i = 2; i < F.length; i++){
			F[i] = F[i-1] + F[i-2];
		}
		//查询数组长度在斐波那契数列中的位置
		while(array.length > F[k]-1){
			k++;
		}
		
		//新建一个数组并将其长度扩展至原数组长度大小的在斐波那契数列中下一个数的大小
		//例如：斐波那契数列为 0 1 1 2 3 5 8 13 21
		//如果原数组大小为9则新数组大小为13，后面新增数组类容使用原数组最后一个数进行填充
		int[] newArray = new int[F[k]-1];
		for(int i = 0; i < F[k]-1; i++){
			if(i < array.length){
				newArray[i] = array[i];
			}else{
				newArray[i] = array[array.length-1];
			}
		}
		
		int low = 0;
		int high = array.length-1;
		int mid;
		while(low <= high){
			mid = low + F[k-1] -1; 
			if(key < newArray[mid]){
				high = mid -1;
				k--;
			}else if(key > newArray[mid]){
				low = mid + 1;
				k-=2;
			}else{
				if(mid < array.length-1){
					return mid;
				}else{
					return array.length - 1;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 检查值是否在数组范围之类，如果在返回true,否则返回false
	 * @param array
	 * @param key
	 * @return
	 */
	public boolean checkScope(int[] array, int key){
		if(key < array[0] || key > array[array.length-1]){
			return false;
		}else{
			return true;
		}
	}

}
