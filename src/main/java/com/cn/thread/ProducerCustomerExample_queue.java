package com.cn.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class ProducerCustomerExample_queue {
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        BlockingQueue<Goods> queue = new LinkedBlockingDeque(20);
        executor.execute(new Producer(queue));
        executor.execute(new Customer(queue));
        executor.shutdown();
    }

    static class Producer implements Runnable{

        private final BlockingQueue<Goods> queue;

        public Producer(BlockingQueue queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true){
                Boolean result = queue.offer(new Goods());
                if(result) {
                    System.out.println("生产了一个商品,剩余总商品数:" + queue.size());
                }
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Customer implements Runnable{

        private final BlockingQueue<Goods> queue;

        public Customer(BlockingQueue queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true){
                try {
                    Goods goods = queue.take();
                    System.out.println("消费了一个商品,剩余总商品数:" + queue.size());
                    TimeUnit.MILLISECONDS.sleep((long)(Math.random()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Goods{
    }
}
