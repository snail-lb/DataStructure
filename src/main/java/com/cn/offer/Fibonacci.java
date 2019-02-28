package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-28 22:34
 * 大家都知道斐波那契数列，现在要求输入一个整数n，请你输出斐波那契数列的第n项（从0开始，第0项为0）。 n<=39
 */
public class Fibonacci {

    public static void main(String[] args) {
        int result = fibonacci_3(8);
        System.out.println(result);
    }

    /**
     * 使用递归实现，但是当n比较大的时候容易导致栈溢出
     * 比如计算 f(4)时： f(4) = f(3)+f(2) = f(2)+f(1)+f(1)+f(0) = f(1)+f(0)+f(1)+f(1)+f(0)  这里f(2)计算了两次，f(1)计算了三次，
     * n越大，重复计算就越多
     *
     * @param n
     * @return
     */
    public static int fibonacci_1(int n) {
        if (n < 0 || n > 39) {
            return -1;
        }

        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }

        return fibonacci_1(n - 1) + fibonacci_1(n - 2);
    }

    /**
     * 循环实现
     *
     * @param n
     * @return
     */
    public static int fibonacci_2(int n) {
        if (n < 0 || n > 39) {
            return -1;
        }

        if (n == 0) {
            return 0;
        }

        int tmp1 = 0;
        int tmp2 = 1;
        for (int i = 1; i < n; i++) {
            tmp2 += tmp1;
            tmp1 = tmp2 - tmp1;
        }
        return tmp2;
    }

    /**
     * 尾递归，把结果放到参数中递归
     *
     * @param n
     * @return
     */
    public static int fibonacci_3(int n) {
        if (n < 0 || n > 39) {
            return -1;
        }

        return fibonacci_3_a(n, 0, 1);
    }

    /**
     * 第一次递归时 n为n时，计算的是位置1的结果，当n递减到1时计算的就是位置n的结果了
     * @param n
     * @param result1
     * @param result2
     * @return
     */
    public static int fibonacci_3_a(int n, int result1, int result2) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return result2;
        }

        return fibonacci_3_a(n - 1, result2, result1 + result2);
    }
}
