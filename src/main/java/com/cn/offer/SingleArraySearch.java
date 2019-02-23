package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-23 11:43
 * 剑指offer第二版 39页 数组中重复的数字
 * 题目： 在一个长度为n的数组中所有的数字都在0~（n-1）的范围内，数字中某些数字是重复的，但不知道有几个数字重复了，也不知道数字重复了几次，
 * 请找出数字中任意一个重复的数字。 例如数组{1,2,3,1,0,2,5,3} 那么输出的是重复的数字2或3
 */
public class SingleArraySearch {
    public static void main(String[] args) {
        int[] array = {2,3,5,1,6,4,4};
        int result = singleArraySearch(array);
        System.out.println(result);
    }

    /**
     * 解题思路：
     * 最直接简单的解法是两个循环来查找，但是这样时间复杂度为O(n²),所以不采用这个方法
     * 因为数组中的所有数字都是在0~（n-1）的范围内，所以我们把每个数字放到他的值对应的数组下标下的位置上，如果有两个相同的数字，必定会放到
     * 数组的同一位置，所以只需要在放之前判断下就能知道该数字是否重复, 每个数字最多只需要交换两次就能找到自己的位置了，所以时间复杂度为O(n)
     *
     * @param array
     * @return
     */
    public static int singleArraySearch(int[] array) {
        if (null == array || array.length < 2) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if(array[i] < 0 || array[i] > array.length-1){
                return -1;
            }
        }

        for (int i = 0; i < array.length; i++) {
            while (array[i] != i) {
                int value = array[i];
                //先判断再交换
                if (value == array[value]) {
                    return value;
                } else {
                    array[i] = array[value];
                    array[value] = value;
                }
            }
        }
        return -1;
    }
}
