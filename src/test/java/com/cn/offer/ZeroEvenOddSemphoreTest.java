package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZeroEvenOddSemphoreTest {


    @Test
    public void test() {
        final ZeroEvenOddSemphore zeos = new ZeroEvenOddSemphore(10);
        new Thread(() -> {
            try {
                zeos.even(new ZeroEvenOddSemphore.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                zeos.odd(new ZeroEvenOddSemphore.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                zeos.zero(new ZeroEvenOddSemphore.IntConsumer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}