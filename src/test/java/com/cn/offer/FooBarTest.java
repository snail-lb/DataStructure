package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class FooBarTest {

    private static void bar() {
        System.out.println("bar");
    }

    private static void foo() {
        System.out.print("foo");
    }

    @Test
    public void test() {
        final FooBar fooBar = new FooBar(10);
        new Thread(() -> {
            try {
                fooBar.foo(FooBarTest::foo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                fooBar.bar(FooBarTest::bar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}