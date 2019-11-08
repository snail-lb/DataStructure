package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

public class MedianOfTwoSortedArraysTest {

    @Test
    public void test1() {
        MedianOfTwoSortedArrays mo = new MedianOfTwoSortedArrays();
        int[] nums1 = {1, 2, 3};
        int[] nums2 = {3, 4};
        Assert.assertEquals(3.0, mo.findMedianSortedArrays(nums1, nums2), 0.1);
    }

    @Test
    public void test2() {
        MedianOfTwoSortedArrays mo = new MedianOfTwoSortedArrays();
        int[] nums1 = {1, 2, 3};
        int[] nums2 = {};
        Assert.assertEquals(2, mo.findMedianSortedArrays(nums1, nums2), 0.1);
    }

    @Test
    public void test3() {
        MedianOfTwoSortedArrays mo = new MedianOfTwoSortedArrays();
        int[] nums1 = {1, 1, 2, 2};
        int[] nums2 = {3, 3, 4, 4};
        Assert.assertEquals(2.5, mo.findMedianSortedArrays(nums1, nums2), 0.1);
    }

    @Test
    public void test4() {
        MedianOfTwoSortedArrays mo = new MedianOfTwoSortedArrays();
        int[] nums1 = {1, 2, 3};
        int[] nums2 = {3, 4};
        Assert.assertEquals(3.0, mo.findMedianSortedArrays(nums1, nums2), 0.1);
    }

    @Test
    public void test5() {
        MedianOfTwoSortedArrays mo = new MedianOfTwoSortedArrays();
        int[] nums1 = {1};
        int[] nums2 = {};
        Assert.assertEquals(1, mo.findMedianSortedArrays(nums1, nums2), 0.1);
    }

    @Test
    public void test6() {
        MedianOfTwoSortedArrays mo = new MedianOfTwoSortedArrays();
        int[] nums1 = {3};
        int[] nums2 = {-2, -1};
        Assert.assertEquals(-1, mo.findMedianSortedArrays(nums1, nums2), 0.1);
    }
}