package com.cn.offer;

import org.junit.Test;

import static org.junit.Assert.*;

public class FooBar_awaitTest {

    private static void bar() {
        System.out.println("bar");
    }

    private static void foo() {
        System.out.print("foo");
    }

    @Test
    public void test() {
        final FooBar_await fooBar = new FooBar_await(10);
       /* new Thread(() -> {
            try {
                fooBar.foo(FooBar_awaitTest::foo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                fooBar.bar(FooBar_awaitTest::bar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
    }
}