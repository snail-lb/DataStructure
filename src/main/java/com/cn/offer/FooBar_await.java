package com.cn.offer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 两个不同的线程将会共用一个 FooBar 实例。其中一个线程将会调用 foo() 方法，另一个线程将会调用 bar() 方法。
 * <p>
 * 请设计修改程序，以确保 "foobar" 被输出 n 次。
 * https://leetcode-cn.com/problems/print-foobar-alternately/
 * @date: 2019-11-04 09:58
 */
public class FooBar_await {
    private int n;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private volatile boolean isOutFoo = true;

    public FooBar_await(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (!isOutFoo) {
                    condition.await();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                isOutFoo = false;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (isOutFoo) {
                    condition.await();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printBar.run();
                isOutFoo = true;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}