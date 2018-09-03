package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadCountExample implements Runnable{

    private Counter counter = null;

    public ThreadCountExample(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2000; i++) {
            counter.add();
        }
    }

    public static void main(String[] args){
        int threadNum = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        Counter counter = new Counter();
        for (int i = 0; i < threadNum; i++) {
            ThreadCountExample volatileExample = new ThreadCountExample(counter);
            executor.submit(volatileExample);
        }
        executor.shutdown();

        try {
            //等待直到所有任务完成
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        counter.print();
    }

    static class Counter{
        private Integer num = 0;//普通Integer非线程安全
        private Integer numSync = 0;//Integer  ++同步，线程安全
        private Integer numLock = 0;//Integer ++加锁，线程安全
        private volatile Integer volatileNum = 0;//volatile Integer非线程安全 volatile只能保证可见性，不能保证原子性
        private AtomicInteger atomicNum = new AtomicInteger(0);//原子计数器  线程安全
        ReentrantLock lock = new ReentrantLock();

        public void add(){
            num++;

            synchronized (this){
                numSync++;
            }

            lock.lock();
            try {
                numLock++;
            } finally {
                lock.unlock();
            }

            volatileNum++;

            atomicNum.addAndGet(1);
        }

        public void print(){
            System.out.println("num:" + num + ", " +
                    "numSync:" + numSync + ", " +
                    "numLock:" + numLock + ", " +
                    "volatileNum:" + volatileNum + ", " +
                    "atomicNum:" + atomicNum);
        }
    }
}


