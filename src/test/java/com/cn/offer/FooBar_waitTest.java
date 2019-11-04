package com.cn.offer;

import org.junit.Test;

public class FooBar_waitTest {

    private static void bar() {
        System.out.println("bar");
    }

    private static void foo() {
        System.out.print("foo");
    }

    @Test
    public void test() {
        final FooBar_wait fooBar = new FooBar_wait(10);
        new Thread(() -> {
            try {
                fooBar.foo(FooBar_waitTest::foo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                fooBar.bar(FooBar_waitTest::bar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}