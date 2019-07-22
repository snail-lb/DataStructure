package com.cn.datastructure;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.cn.datastructure.concurrent.MyThreadPoolExecutor;
import com.cn.datastructure.utils.MyRejectedExecutionHandler;

/**
 * 用生产者消费者模式简单测一下
 */
public class MyThreadPoolExecutorTest3 {

    private static MyThreadPoolExecutor executor = new MyThreadPoolExecutor(1, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) throws InterruptedException {
//        test1();
        test2();
    }

    public static void test1() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            executor.submit(new Work(i));
        }
        System.out.println("任务总数：" + executor.getTaskCount());
        System.out.println("已完成任务数" + executor.getCompletedTaskCount());
        executor.shutdown();
//        executor.execute(new Work(6));
        System.out.println("+++++");
        executor.awaitTermination(1000, TimeUnit.SECONDS);
        System.out.println("线程执行完成");
        System.out.println("已完成任务数" + executor.getCompletedTaskCount());
    }
    public static void test2() throws InterruptedException {
        Future<Integer> future = executor.submit(new Work(5));
        try {
            Integer result = future.get(100, TimeUnit.MILLISECONDS);
            System.out.println("获取到任务返回结果：" + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();

            boolean cancel = future.cancel(true);
            if(cancel) {
                System.out.println("取消成功");
            }else{
                System.out.println("取消失败");
            }
        }
        executor.shutdown();
    }


    static class Work implements Callable<Integer> {
        private Integer num = null;
        public Work(int n){
            num = n;
        }

        public Integer getNum() {
            return num;
        }

        @Override
        public Integer call() throws Exception {
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("工作:" + num);
            return num;
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