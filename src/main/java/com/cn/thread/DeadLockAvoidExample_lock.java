package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 避免死锁样例
 * @date: 2018-09-12 22:20
 */
public class DeadLockAvoidExample_lock {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();
        executor.execute(new ExeThread_1(lock1, lock2));
        executor.execute(new ExeThread_2(lock1, lock2));
        executor.shutdown();
    }

    static class ExeThread_1 implements Runnable {
        private final ReentrantLock lock1;
        private final ReentrantLock lock2;

        public ExeThread_1(ReentrantLock lock1, ReentrantLock lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        @Override
        public void run() {
            while (true) {
                if (lock1.tryLock()) {
                    try {
                        System.out.println("线程1获取到lock1的锁了");
                        if (lock2.tryLock()) {
                            //如果获取锁成功就执行业务逻辑，否则就释放lock1的锁，自选重新尝试获取锁
                            try {
                                System.out.println("线程1获取到lock2的锁了");
                                break;
                            } finally {
                                lock2.unlock();
                            }
                        }else{
                            System.out.println("线程1获取到lock2的锁失败,释放lock1的锁再次获取");
                        }
                    } finally {
                        lock1.unlock();
                    }
                }
            }
        }
    }

    static class ExeThread_2 implements Runnable {
        private final ReentrantLock lock1;
        private final ReentrantLock lock2;

        public ExeThread_2(ReentrantLock lock1, ReentrantLock lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
        }

        @Override
        public void run() {
            while (true) {
                if (lock2.tryLock()) {
                    try {
                        System.out.println("线程2获取到lock2的锁了");
                        if (lock1.tryLock()) {
                            try {
                                System.out.println("线程2获取到lock1的锁了");
                                break;
                            } finally {
                                lock1.unlock();
                            }
                        }else{
                            System.out.println("线程2获取到lock1的锁失败,释放lock2的锁再次获取");
                        }
                    } finally {
                        lock2.unlock();
                    }
                }
            }
        }
    }
}
