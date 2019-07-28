package com.cn.datastructure;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cn.datastructure.concurrent.MyThreadPoolExecutor;
import com.cn.datastructure.utils.MyRejectedExecutionHandler;

/**
 * 用生产者消费者模式简单测一下
 */
public class MyThreadPoolExecutorTest2 {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            executor.execute(new Work(i));
        }
        System.out.println(executor.getTaskCount());
        System.out.println(executor.getCompletedTaskCount());
        executor.shutdown();
//        executor.execute(new Work(6));
        System.out.println("+++++");
        executor.awaitTermination(1000, TimeUnit.SECONDS);
        System.out.println("线程执行完成");
        System.out.println(executor.getCompletedTaskCount());
    }


    static class Work implements Runnable{
        private Integer num = null;
        public Work(int n){
            num = n;
        }

        public Integer getNum() {
            return num;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("工作:" + num);
        }
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            return t;
        }
    }

    public static class AbortPolicy implements MyRejectedExecutionHandler {

        public AbortPolicy() {
        }

        @Override
        public void rejectedExecution(Runnable r, MyThreadPoolExecutor e) {
            System.out.println("丢弃任务：" + ((Work)r).getNum());
        }
    }
}