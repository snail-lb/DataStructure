package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerCustomerExample_await {
    public static void main(String[] args){
        Goods goods = new Goods();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        executor.execute(new Producer(goods, lock, condition));
        executor.execute(new Customer(goods, lock, condition));
        executor.shutdown();
    }

    static class Producer implements Runnable{

        private Goods goods = null;

        private final Lock lock;

        private final Condition condition;

        public Producer(Goods goods, Lock lock, Condition condition){
            this.goods = goods;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (true){
                lock.lock();
                try {
                    //使用循环进行等待，相当于一个简单的自旋锁，防止虚假唤醒
                    while (goods.getTotal() > 5){
                        try {
                            System.out.println("商品数已达上限，等待消费");
                            condition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    goods.add();
                    //notify和notifyAll在这里都可以使用，因为一共只有两个线程
                    condition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
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

        private Goods goods = null;

        private final Lock lock;

        private final Condition condition;

        public Customer(Goods goods, Lock lock, Condition condition){
            this.goods = goods;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (true){
                lock.lock();
                try {
                    while (goods.getTotal() < 4){
                        try {
                            System.out.println("商品已经快卖完，等待生产");
                            condition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    goods.del();
                    condition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep((long)(Math.random()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Goods{
        private Integer total = 3;

        public void add() {
            total++;
            System.out.println("生产商品:" + total);
        }

        public void del() {
            total--;
            System.out.println("消费商品:" + total);
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }
}
