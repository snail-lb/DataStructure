package com.cn.thread;

public class RunnableExample implements Runnable{

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        System.out.println("启动一个线程:" + thread.getName() + ",执行情况：" + thread.getState());
    }

    public static void main(String[] args){
        RunnableExample threadExample = new RunnableExample();
        Thread thread = new Thread(threadExample);
        thread.setName("RunnableExample");
        thread.start();

        Thread mainThread = Thread.currentThread();
        System.out.println("主线程线程:" + mainThread.getName() + ",执行情况：" + mainThread.getState());
    }
}
