package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZeroEvenOddNoLockTest {
    @Test
    public void test() throws InterruptedException {
        final ZeroEvenOddNoLock zeonl = new ZeroEvenOddNoLock(10);
        Thread thread1 = new Thread(() -> {
            try {
                zeonl.even(new ZeroEvenOddNoLock.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                zeonl.odd(new ZeroEvenOddNoLock.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread2.start();

        Thread thread3 =  new Thread(() -> {
            try {
                zeonl.zero(new ZeroEvenOddNoLock.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }
}