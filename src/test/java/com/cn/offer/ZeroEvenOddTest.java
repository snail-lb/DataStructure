package com.cn.offer;

import org.junit.Test;

public class ZeroEvenOddTest {

    @Test
    public void test() {
        final ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(10);
        new Thread(() -> {
            try {
                zeroEvenOdd.even(new ZeroEvenOdd.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                zeroEvenOdd.odd(new ZeroEvenOdd.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                zeroEvenOdd.zero(new ZeroEvenOdd.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}