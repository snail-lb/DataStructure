package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-23 16:38
 * 剑指offer第二版 44页 二维数组中的查找
 * 题目： 在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下的递增的顺序排序，请完成一个函数，输入这样的一个二维数组和一个
 * 整数，判断数组中是否含有该整数
 */
public class DoubleArraySearch {
    public static void main(String[] args) {
        int[][] array = {
                {1, 2, 8, 9},
                {2, 4, 9, 12},
                {4, 7, 10, 13},
                {6, 8, 11, 15},
                {7, 8, 11, 15},
        };
        boolean result = doubleArraySearch(array, 9);
        System.out.println(result);
    }

    /**
     * 解题思路：
     * 从第一行从右到左搜索一个array[n]<target && array[n+1]>target的n,然后从位置n开始向下搜索,
     * 如果有array[n]==target或者array[n+1]==target就不用搜了
     * 在第n列中搜array[n][m]<target && array[n][m+1]>target 同上有等于target的就不用搜了
     * 找到m、n后可以确定target的下标位置：0<=x<=n  (m+1)<=y<=y.length()
     * 然后重复以上搜索方式，直到找到target或者搜索到位置array[0][y.length()-1]结束没找到
     *
     *
     * @param array
     * @return
     */
    public static boolean doubleArraySearch(int[][] array, int target) {
        if (null == array || array.length == 0 || array[0].length == 0) {
            return false;
        }
        int xLength = array.length;
        int yLength = array[0].length;

        if(array[0][0] > target || array[xLength-1][yLength-1] < target){
            return false;
        }

        int n = yLength -1; //表示列数
        int m = 0; //表示行数

        for (; n >= 0; n--) {
            System.out.println(m + "," + n);
            if(array[m][n] == target){
                //找到了
                System.out.println("最终位置" + m + "," + n);
                return true;
            }

            if (n > 0 && array[m][n] > target && array[m][n-1] > target){
                //向左搜索
                continue;
            } else if(n > 0 && array[m][n] > target && array[m][n-1] == target){
                //找到了
                System.out.println("最终位置" + m + "," + (n-1));
                return true;
            } else if(n > 0 && array[m][n] > target && array[m][n-1] < target){
                //从下一列向下搜索
                n--;
            }

            //向下搜索
            for(; m <= yLength-1; m++){
                System.out.println(m + "," + n);
                if(array[m][n] == target){
                    //找到了
                    return true;
                }

                if(m<yLength-1 && array[m][n] < target && array[m+1][n] < target){
                    //继续向下搜索
                    continue;
                }else if(m<yLength-1 && array[m][n] < target && array[m+1][n] == target){
                    //找到了
                    System.out.println("最终位置" + (m+1) + "," + n);
                    return true;
                }else if(m<yLength-1 && array[m][n] < target && array[m+1][n] > target){
                    //从下一行向左搜索
                    m++;
                }

                //向左搜索
                break;
            }

        }
        return false;
    }

    /**
     * 网上更简单的写法，思路大概是差不多的
     * @param array
     * @param target
     * @return
     */
    public static boolean doubleArraySearchSimple(int[][] array, int target) {
        int row=0;
        int col=array[0].length-1;
        while(row<=array.length-1&&col>=0){
            if(target==array[row][col])
                return true;
            else if(target>array[row][col])
                row++;
            else
                col--;
        }
        return false;
    }
}
