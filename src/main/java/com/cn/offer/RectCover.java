package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-03-14 22:29
 * 我们可以用2*1的小矩形横着或者竖着去覆盖更大的矩形。请问用n个2*1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
 */
public class RectCover {
    public static void main(String[] args){
        int n = rectCover(5);
        System.out.println(n);
    }

    /**
     * 思路和跳台阶差不多，
     * f(n) = 1   n==1
     * f(n) = 2   n==2
     * f(n) = f(n-1) + f(n-2)   n>2
     * @param target
     * @return
     */
    public static int rectCover(int target) {
        if(target  < 1){
            return 0;
        }
        if(target == 1){
            return 1;
        }else if(target == 2){
            return 2;
        }else{
            return rectCover(target-1)+rectCover(target-2);
        }
    }
}
