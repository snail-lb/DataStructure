package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class FooBar_volatileTest {

    private static void bar() {
        System.out.println("bar");
    }

    private static void foo() {
        System.out.print("foo");
    }

    @Test
    public void test() {
        final FooBar_volatile fooBar = new FooBar_volatile(10);
        new Thread(() -> {
            try {
                fooBar.foo(FooBar_volatileTest::foo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                fooBar.bar(FooBar_volatileTest::bar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}