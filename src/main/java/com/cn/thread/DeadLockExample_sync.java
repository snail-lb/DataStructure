package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 死锁样例  相互等待对方的资源
 * @date: 2018-09-12 22:20
 */
public class DeadLockExample_sync {
    public static void main(String[] args){
        Object obj1 = new Object();
        Object obj2 = new Object();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ExeThread_1(obj1, obj2));
        executor.execute(new ExeThread_2(obj1, obj2));
        executor.shutdown();
    }

    static class ExeThread_1 implements Runnable{
        private final Object obj1;
        private final Object obj2;

        public ExeThread_1(Object obj1, Object obj2) {
            this.obj1 = obj1;
            this.obj2 = obj2;
        }

        @Override
        public void run() {
            synchronized (obj1){
                System.out.println("线程1获取到Obj1的锁了");
                //等待一秒让另一个线程能获取到第一个锁
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2){
                    System.out.println("线程1获取到Obj2的锁了");
                }

            }
        }
    }

    static class ExeThread_2 implements Runnable{
        private final Object obj1;
        private final Object obj2;

        public ExeThread_2(Object obj1, Object obj2) {
            this.obj1 = obj1;
            this.obj2 = obj2;
        }

        @Override
        public void run() {
            synchronized (obj2){
                System.out.println("线程2获取到Obj2的锁了");
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj1){
                    System.out.println("线程2获取到Obj1的锁了");
                }
            }
        }
    }
}
