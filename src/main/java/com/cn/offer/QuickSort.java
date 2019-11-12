package com.cn.offer;

/**
 * @author: lvbiao
 * @date: 2019-11-12 10:37
 */
public class QuickSort {

    public void quickSort(int[] arrays) {
        qSort(arrays, 0, arrays.length - 1);
    }

    private void qSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = quickSortAssist(array, left, right);
            qSort(array, left, mid - 1);
            qSort(array, mid + 1, right);
        }
    }

    /**
     * 辅助函数，主要对指定数组范围类的数据进行切割，从返回位置切割，使得左边的数均小于等于右边的数
     *
     * @param arrays
     * @param start
     * @param end
     */
    private int quickSortAssist(int[] arrays, int start, int end) {
        if (arrays == null || start < 0 || end < 0) {
            return -1;
        }
        int value = arrays[start];
        while (start < end) {
            while (start < end && arrays[end] > value) {
                end--;
            }
            swap(arrays, start, end);
            while (start < end && arrays[start] <= value) {
                start++;
            }
            swap(arrays, start, end);
        }
        arrays[start] = value;
        return start;
    }

    /**
     * 交换数组中两个数的位置
     *
     * @param arrays
     * @param left
     * @param right
     */
    private void swap(int[] arrays, int left, int right) {
        if (arrays == null || left < 0 || right < 0) {
            return;
        }
        int tmp = arrays[left];
        arrays[left] = arrays[right];
        arrays[right] = tmp;
    }

}
