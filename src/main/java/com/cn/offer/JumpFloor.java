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
        int n = jumpFloor(10);
        System.out.println(n);
    }

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
}
