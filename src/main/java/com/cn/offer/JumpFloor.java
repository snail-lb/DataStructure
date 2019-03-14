package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-03-02 14:08
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法（先后次序不同算不同的结果）。
 */
public class JumpFloor {

    public static void main(String[] args) {
        int n = jumpFloorII(5);
        System.out.println(n);
    }

    /**
     * 跳台阶每多跳一次有两种跳发，要么跳一个台阶，要么跳两个台阶
     * 比如第n次的的次数：如果最后增加跳的那次跳了一个台阶，那么跳发和n-1次的跳发是相等的，如果最后那次跳了两个台阶，那跳发和n-2次
     * 的跳发是相等的，所以 f(n) = f(n-1) + f(n-2)
     *
     * @param target
     * @return
     */
    public static int jumpFloor(int target) {
        if (target < 1) {
            return 0;
        }

        if (target == 1) {
            return 1;
        }

        if (target == 2) {
            return 2;
        }

        return jumpFloor(target - 1) + jumpFloor(target - 2);
    }

    /**
     * 延伸： 一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
     * 解法思想： 和jumpFloor思想类似，可以推导出
     * f(n) = f(n-1) + f(n-2) + f(n-3) + ... + f(0)   条件n>1
     * f(0) = 1
     * f(1) = 1
     *
     * 推导：
     * f(n)   = f(n-1) + f(n-2) + f(n-3) + ... + f(0)
     * f(n-1) =          f(n-2) + f(n-3) + ... + f(0)
     * 相减： f(n) = 2 * f(n-1)
     * @param target
     * @return
     */
    public static int jumpFloorII(int target) {
        if (target < 0) {
            return 0;
        }

        if (target == 1 | target == 0) {
            return 1;
        }
        return 2 * jumpFloorII(target - 1) ;
    }
}
