package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-23 19:20
 * 剑指offer第二版 110页 数值的整数次方
 * 题目：给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
 */
public class Power {
    public static void main(String[] args){
        System.out.println(power(5, -3));
    }

    /**
     * 不需要考虑返回结果超出double表达范围的情况
     * @param base
     * @param exponent
     * @return
     */
    public static double power(double base, int exponent) {
        if(base == 0 && exponent <= 0){
            throw new RuntimeException();
        }

        double result = 1.0;
        if (exponent > 0 ){
            for (int i = 0; i < exponent; i++) {
                result = result * base;
            }
        } else if (exponent < 0 ){
            for (int i = 0; i > exponent; i--) {
                result = result * 1.0/base;
            }
        }
        return result;
    }
}
