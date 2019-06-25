package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MutexLockTest {

    private volatile static Integer count = 0;

    private static MutexLock lock = new MutexLock();

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 1; i <= 10; i++) {
            executor.execute(new MutexLockTest.ExeThread());
        }
    }

    static class ExeThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 不加锁
            /*count++;
            System.out.println(count);*/

            // 使用同步代码块
            /*synchronized (MutexLockTest.class) {
                count++;
                System.out.println(count);
            }*/

            //使用自定义锁
            try {
                lock.lock();
                count++;
                System.out.println(count);
            } finally {
                lock.unlock();
            }
        }
    }
}