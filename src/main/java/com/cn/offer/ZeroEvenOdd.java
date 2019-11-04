package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 *
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 每个线程都有一个 printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 *
 * https://leetcode-cn.com/problems/print-zero-even-odd/
 * @date: 2019-11-04 10:57
 */
public class ZeroEvenOdd {
    private int n;
    private Object lock = new Object();
    private volatile boolean isZero = true;
    private volatile int index = 1;

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (index <= n) {
            synchronized (lock) {
                while (!isZero) {
                    lock.wait();
                }

                if (index <= n) {
                    printNumber.accept(0);
                }
                isZero = false;
                lock.notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (index <= n) {
            synchronized (lock) {
                while (isZero || (index & 1) != 0) {
                    lock.wait();
                }
                if (index <= n) {
                    printNumber.accept(index);
                }
                isZero = true;
                index++;
                lock.notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (index <= n) {
            synchronized (lock) {
                while (isZero || (index & 1) == 0) {
                    lock.wait();
                }

                if (index <= n) {
                    printNumber.accept(index);
                }
                isZero = true;
                index++;
                lock.notifyAll();
            }
        }
    }

    protected static class IntConsumer {
        public void accept(int x) {
            System.out.println(x);
        }
    }
}