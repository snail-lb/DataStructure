package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 两个不同的线程将会共用一个 FooBar 实例。其中一个线程将会调用 foo() 方法，另一个线程将会调用 bar() 方法。
 * <p>
 * 请设计修改程序，以确保 "foobar" 被输出 n 次。
 * https://leetcode-cn.com/problems/print-foobar-alternately/
 * @date: 2019-11-04 09:58
 */
public class FooBar {
    private int n;
    private Object lock = new Object();
    private volatile boolean isOutFoo = true;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (!isOutFoo) {
                    lock.wait();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                isOutFoo = false;

                lock.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (isOutFoo) {
                    lock.wait();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                isOutFoo = true;

                lock.notifyAll();
            }
        }
    }
}