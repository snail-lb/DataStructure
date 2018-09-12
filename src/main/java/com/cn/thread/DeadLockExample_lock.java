package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 死锁样例
 * @date: 2018-09-12 22:20
 */
public class DeadLockExample_lock {
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock  lock1 = new ReentrantLock();
        ReentrantLock  lock2 = new ReentrantLock();
        executor.execute(new ExeThread_1(lock1, lock2));
        executor.execute(new ExeThread_2(lock1, lock2));
        executor.shutdown();
    }

    static class ExeThread_1 implements Runnable{
        private final ReentrantLock  lock1;
        private final ReentrantLock  lock2;

        public ExeThread_1(ReentrantLock  lock1, ReentrantLock  lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        @Override
        public void run() {
            try {
                lock1.lock();
                System.out.println("线程1获取到lock1的锁了");
                //等待一秒让另一个线程能获取到第一个锁
                TimeUnit.SECONDS.sleep(3L);
                lock2.lock();
                System.out.println("线程1获取到lock2的锁了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if(lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
            }
        }
    }

    static class ExeThread_2 implements Runnable{
        private final ReentrantLock  lock1;
        private final ReentrantLock  lock2;

        public ExeThread_2(ReentrantLock  lock1, ReentrantLock  lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        @Override
        public void run() {
            try {
                lock2.lock();
                System.out.println("线程2获取到lock2的锁了");
                TimeUnit.SECONDS.sleep(3L);
                lock1.lock();
                System.out.println("线程2获取到lock1的锁了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if(lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
            }
        }
    }
}
