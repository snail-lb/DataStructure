package com.cn.thread;

import java.util.concurrent.TimeUnit;

public class JoinExample implements Runnable{

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new JoinExample());
        System.out.println("线程1启动时间：" + System.currentTimeMillis());
        thread1.start();
        thread1.join();//主线程在这里会被阻塞，直到thread1结束
        System.out.println("主线程执行时间：" + System.currentTimeMillis());

        Thread thread2 = new Thread(new JoinExample());
        System.out.println("线程2启动时间：" + System.currentTimeMillis());
        thread2.start();
        System.out.println("主线程执行时间：" + System.currentTimeMillis());
    }
}
