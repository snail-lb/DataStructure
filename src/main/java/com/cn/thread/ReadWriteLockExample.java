package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 读写锁样例
 * @date: 2018-09-16 10:21
 */
public class ReadWriteLockExample {
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReadWrite readWrite = new ReadWrite();
        executor.execute(() -> {
            while (true) {
                readWrite.read();
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executor.execute(() -> {
            while (true) {
                readWrite.write();
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static class ReadWrite{
        private Integer num = 1;
        private ReadWriteLock lock = new ReentrantReadWriteLock();

        public void read(){
            lock.readLock().lock();
            try {
                System.out.println("读取数据num:" + num);
            } finally {
                lock.readLock().unlock();
            }
        }

        public void write(){
            lock.writeLock().lock();
            try {
                num++;
                System.out.println("写入数据num:" + num);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
}
