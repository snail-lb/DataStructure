package com.cn.datastructure;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cn.datastructure.concurrent.MyLinkedBlockingQueue;

/**
 * 用生产者消费者模式简单测一下
 */
public class MyLinkedBlockingQueueTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        BlockingQueue<Goods> queue = new MyLinkedBlockingQueue(20);
//        BlockingQueue<Goods> queue = new LinkedBlockingQueue(20);
        for (int i = 0; i < 5; i++) {
            executor.execute(new Producer(queue));
            executor.execute(new Customer(queue));
        }
        executor.shutdown();
    }

    static class Producer implements Runnable {

        private final BlockingQueue<Goods> queue;

        public Producer(BlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    queue.put(new Goods());
                    System.out.println("生产了一个商品,剩余总商品数:" + queue.size());
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    static class Customer implements Runnable {

        private final BlockingQueue<Goods> queue;

        public Customer(BlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Goods goods = queue.take();
                    System.out.println("消费了一个商品,剩余总商品数:" + queue.size());
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Goods {
        private static final String test = "test";
    }
}
