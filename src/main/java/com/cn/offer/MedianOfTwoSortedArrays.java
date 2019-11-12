package com.cn.offer;

/**
 * @author: lvbiao
 * @date: 2019-11-07 09:39
 * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
 * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
 * 你可以假设 nums1 和 nums2 不会同时为空。
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 */
public class MedianOfTwoSortedArrays {

    /**
     * 思路： 1. 从两个数组的左边去数（先取最小的）, 直到取出一半（两个数组长度和的一半），就能找到中位数，但是时间负责度是 O( (m+n)/2 ),满足不了要求
     * 2. 使用二分法： 中位数要求左边均小于等于该数，右边均大于等于该数，分别取两个数组的中间，如果数组一的中位数小于数组二的中位数，就继续在
     * 数组一的右半部分和数组二的左半部分中继续查找，直到找到为止
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int m = nums2.length;

        //保证数组1一定最短
        if (n > m) {
            return findMedianSortedArrays(nums2, nums1);
        }

        // Ci 为第i个数组的割,比如C1为2时表示第1个数组只有2个元素。LMaxi为第i个数组割后的左元素。RMini为第i个数组割后的右元素。
        int LMax1 = 0, LMax2 = 0, RMin1 = 0, RMin2 = 0, c1, c2, lo = 0, hi = 2 * n;  //我们目前是虚拟加了'#'所以数组1是2*n长度

        //二分
        while (lo <= hi) {
            c1 = (lo + hi) / 2;  //c1是二分的结果
            c2 = m + n - c1;

            LMax1 = (c1 == 0) ? Integer.MIN_VALUE : nums1[(c1 - 1) / 2];
            RMin1 = (c1 == 2 * n) ? Integer.MAX_VALUE : nums1[c1 / 2];
            LMax2 = (c2 == 0) ? Integer.MIN_VALUE : nums2[(c2 - 1) / 2];
            RMin2 = (c2 == 2 * m) ? Integer.MAX_VALUE : nums2[c2 / 2];

            if (LMax1 > RMin2) {
                hi = c1 - 1;
            } else if (LMax2 > RMin1) {
                lo = c1 + 1;
            } else {
                break;
            }
        }
        return (Math.max(LMax1, LMax2) + Math.min(RMin1, RMin2)) / 2.0;

    }
}
