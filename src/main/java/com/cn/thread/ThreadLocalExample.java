package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:  ThreadLocal使用样例
 * ThreadLocal类用来提供线程内部的局部变量。这些变量在多线程环境下访问(通过get或set方法访问)时能保证各个线程里的变量相对独立于其他线程内的变量
 * @date: 2018-09-18 22:40
 */
public class ThreadLocalExample {
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Task task = new Task();
        for (int i = 0; i < 10; i++) {
            executor.submit(task);
        }
        executor.shutdown();
    }

    static class Task implements Runnable{
        private Long unsafeData;
        private ThreadLocal<Long> safeData = new ThreadLocal<>();

        @Override
        public void run() {
            this.unsafeData = Thread.currentThread().getId();
            this.safeData.set(Thread.currentThread().getId());
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("ThreadID: %s unsafeDate: %d safeDate: %d\n", Thread.currentThread().getId(), this.unsafeData, this.safeData.get());

        }
    }
}
