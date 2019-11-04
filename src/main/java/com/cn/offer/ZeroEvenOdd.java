package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
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