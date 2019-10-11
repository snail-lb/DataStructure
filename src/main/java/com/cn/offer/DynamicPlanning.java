package com.cn.offer;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript: 从一个数组中找到和最大的子数组的和
 * @date: 2019-10-11 10:18
 */
public class DynamicPlanning {
    public static void main(String[] args) {
        int[] array = {-1,2,8,-13,4,5,2};
        System.out.println(maxSubSequence(array));

    }

    /**
     * 动态规划状态方程： sum[i] = Max{ sum[i-1] + array[i], array[i]};
     * sum[i]表示以i结尾的数组中 子数组的最大和
     * @param array
     * @return
     */
    public static int maxSubSequence(int[] array) {
        // max = Max{sum[i-1] + array[i], array[i]}
        int sum[] = new int[array.length];
        sum[0] = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            sum[i] = Math.max(sum[i-1] + array[i], array[i]);
            if(sum[i] > max){
                max = sum[i];
            }
        }
        return max;
    }

}
