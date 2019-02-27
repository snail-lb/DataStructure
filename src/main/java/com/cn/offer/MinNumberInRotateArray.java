package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-27 22:26
 * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。 输入一个非减排序的数组的一个旋转，输出旋转数组的最小元素。
 * 例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。 NOTE：给出的所有元素都大于0，若数组大小为0，请返回0。
 */
public class MinNumberInRotateArray {
    public static void main(String[] args) {
//        int[] array = {5,6,7,8,9,1,2,3,4};
        int[] array = {1, 1, 2, 3, 0, 1, 1, 1, 1, 1};
        int result = minNumberInRotateArray(array);
        System.out.println(result);
    }

    /**
     * 思路： 问题可以简化为在两个有序数组中找最小值，可以利用二分法查找，查找时与旋转数组的最后一个进行比较，
     * 如果大于该值就向后查找，如果小于该值就向前找，直到无法分割为止
     *
     * @param array
     * @return
     */
    public static int minNumberInRotateArray(int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        return dichotomy(array, 0, array.length - 1);
    }

    public static int dichotomy(int[] array, int startIndex, int endIndex) {
        if (endIndex - startIndex == 1) {
            return array[endIndex];
        }

        int lastValue = array[array.length - 1];
        int middleIndex = (startIndex + endIndex) / 2;
        if (array[middleIndex] > lastValue) {
            //向后查找
            return dichotomy(array, middleIndex, endIndex);
        } else if (array[middleIndex] < lastValue) {
            //向前查找
            return dichotomy(array, startIndex, middleIndex);
        } else {
            //相等时，从开始向后找一个与开始数字不相等，从后向前找一个与开始数字不相等的地方 继续之前的查找步骤，
            while (endIndex - startIndex > 1) {
                if (array[startIndex] == lastValue) {
                    startIndex++;
                }
                if (array[endIndex] == lastValue) {
                    endIndex--;
                }
                if (array[startIndex] != lastValue && array[endIndex] != lastValue) {
                    break;
                }
            }
            if (endIndex - startIndex == 1) {
                //消除相同的刚好剩下两个，返回小的那个,相等的话就无所谓了
                return array[startIndex] < array[endIndex] ? array[startIndex] : array[endIndex];
            }
            return dichotomy(array, startIndex, endIndex);
        }
    }
}
