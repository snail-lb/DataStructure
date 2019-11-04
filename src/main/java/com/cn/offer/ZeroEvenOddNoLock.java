package com.cn.offer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 * <p>
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 每个线程都有一个 printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 * <p>
 * https://leetcode-cn.com/problems/print-zero-even-odd/
 */
public class ZeroEvenOddNoLock {
    private int n;
    private AtomicInteger state = new AtomicInteger(0);


    public ZeroEvenOddNoLock(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            while (state.get() != 0) {
                Thread.yield();
            }
            printNumber.accept(0);
            if ((i & 1) == 0) {
                while (!state.compareAndSet(0, 2)) {
                    Thread.yield();
                }
            } else {
                while (!state.compareAndSet(0, 1)) {
                    Thread.yield();
                }
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            while (state.get() != 2) {
                Thread.yield();
            }
            printNumber.accept(i);
            while (!state.compareAndSet(2, 0)) {
                Thread.yield();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            while (state.get() != 1) {
                Thread.yield();
            }
            printNumber.accept(i);
            while (!state.compareAndSet(1, 0)) {
                Thread.yield();
            }
        }
    }

    protected static class IntConsumer {
        public void accept(int x) {
            System.out.println(x);
        }
    }
}
