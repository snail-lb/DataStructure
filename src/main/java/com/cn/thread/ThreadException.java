package com.cn.thread;

import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 线程异常处理
 * @date: 2018-09-18 23:02
 */
public class ThreadException {
    private static volatile String message=null;
    public static void main(String[] args) throws InterruptedException {
        //主线程这里是捕获不到子线程抛出的异常的
        try {
            Thread thread_simple = new Thread(new Task());
            thread_simple.start();
            thread_simple.join();
        } catch (Exception e) {
            System.out.println(String.format("主线程捕获子线程的异常:%s", e.getMessage()));
        }

        TimeUnit.SECONDS.sleep(1L);
        System.out.println("++++++++线程1执行完毕++++++++");

        //可以自定义异常捕获处理器
        Thread thread_uncaught = new Thread(new Task());
        thread_uncaught.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        thread_uncaught.start();
        thread_uncaught.join();

        TimeUnit.SECONDS.sleep(1L);
        System.out.println("++++++++线程2执行完毕++++++++");
        if(message != null){
            System.out.println(message);
        }
    }

    static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(String.format("自定义异常处理：这里处理线程%s抛出的异常:%s", t.getId(), e.getMessage()));
        }
    }

    static class Task implements Runnable{
        @Override
        public void run() {
            message = "使用共享变量记录线程执行出现的错误";
            throw new RuntimeException("线程执行出现错误");
        }
    }
}
