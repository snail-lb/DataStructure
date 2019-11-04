package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-11-04 10:32
 */
public class FooBar_volatile {
    private volatile boolean isOutFoo = true;
    private int n;

    public FooBar_volatile(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (!isOutFoo) {
                Thread.yield();
            }

            printFoo.run();
            isOutFoo = false;
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (isOutFoo) {
                Thread.yield();
            }

            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            isOutFoo = true;
        }
    }
}
