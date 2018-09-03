package com.cn.thread;

public class ThreadExample extends Thread{
    @Override
    public void run() {
        this.setName("ThreadExample");
        System.out.println("启动一个线程:" + this.getName() + ",执行情况：" + this.getState());
    }

    public static void main(String[] args){
        ThreadExample threadExample = new ThreadExample();
        threadExample.start();

        Thread mainThread = Thread.currentThread();
        System.out.println("主线程线程:" + mainThread.getName() + ",执行情况：" + mainThread.getState());
    }
}
