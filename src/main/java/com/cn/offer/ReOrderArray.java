package com.cn.offer;

import java.util.Arrays;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-24 22:19
 * 题目描述
 * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，所有的偶数位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。
 */
public class ReOrderArray {
    public static void main(String[] args) {
        int[] array = {1, 5, 2, 4, 7, 9, 8};
        reOrderArray(array);
        System.out.println(Arrays.toString(array));
    }

    /**
     * 思路： 从左到右依次遍历，找到第一个偶数，然后从这个位置开始向后找到第一个奇数，然后将偶数到这个奇数前一个数全部向后移动意味，最后将奇数放到第一个偶数
     * 的位置，继续再向后找奇数依次进行
     * 如： {1, 5, 2, 4, 7, 9, 8}
     *  第一次交换后： {1, 5, 7, 2, 4, 9, 8}
     *  第二次交换后： {1, 5, 7, 9, 2, 4, 8}
     * @param array
     */
    public static void reOrderArray(int[] array) {
        if (null == array || array.length == 0) {
            return;
        }

        int event_index = -1; //数组中第一个偶数的位置索引
        for (int i = 0; i < array.length; i++) {
            if (event_index == -1 && array[i] % 2 == 0) {
                //找到第一个偶数的位置
                event_index = i;
            }
            if (event_index != -1 && array[i] % 2 != 0) {
                // 在找到第一个偶数位置之后找到第一个奇数
                //进行移位
                int odd = array[i];
                for(int j = i; j > event_index; j--){
                    array[j] = array[j-1];
                }
                array[event_index] = odd;
                // 偶数因为向后移动了一位，所以加一
                event_index++;
            }
        }
    }
}
