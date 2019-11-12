package com.cn.offer;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuickSortTest {

    @Test
    public void quickSort() {
        int[] arrays = {3,2,5,4,2,1,7,8};
        QuickSort quickSort = new QuickSort();
        quickSort.quickSort(arrays);
        System.out.println(Arrays.toString(arrays));
    }
}