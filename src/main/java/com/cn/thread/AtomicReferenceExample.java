package com.cn.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 原子引用样例
 * @date: 2018-09-16 11:16
 */
public class AtomicReferenceExample {
    private static Integer num = 0;
    private static AtomicReference<Integer> atomicReference = new AtomicReference<>(0);

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future future1 = executor.submit(new Task());
        Future future2 = executor.submit(new Task());
        Future future3 = executor.submit(new Task());
        executor.shutdown();
        try {
            future1.get();
            future2.get();
            future3.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //这里就可以看出，num每次执行结果都不一样， 而atomicReference每次结果都是一样的切符合我们的预期值
        System.out.println("num:" + num + "\natomicReference:" + atomicReference.get());
    }

    static class Task implements Runnable{
        public void run(){
            for (int i = 0; i < 5000; i++) {
                num++;
                boolean flag;
                do{
                    Integer result = atomicReference.get();
                    //如果设置值失败就需要不断重试，直到成功为止 CAS(Compare And Set)操作
                    flag = atomicReference.compareAndSet(result, result + 1);
                }while (!flag);
            }
        }
    }
}