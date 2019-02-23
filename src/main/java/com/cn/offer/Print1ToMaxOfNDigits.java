package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-23 19:41
 * 剑指offer第二版 114页  打印1到最大的n位数
 * 题目：给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
 */
public class Print1ToMaxOfNDigits {
    public static void main(String[] args){
        pint1ToMaxOfNDigits(9);
    }

    public static void pint1ToMaxOfNDigits(int n){
        if(n <= 0 || n >= new Integer(Integer.MAX_VALUE).toString().length()){
            return;
        }


        char[] c = new char[n];
        for (int i = 0; i < n; i++) {
            c[i] = '9';
        }

        int max = Integer.parseInt(new String(c));
        for (int i = 1; i <= max; i++) {
            System.out.println(i);
        }
    }
}
