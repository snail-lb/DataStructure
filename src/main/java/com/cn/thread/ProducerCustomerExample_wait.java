package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProducerCustomerExample_wait {
    public static void main(String[] args){
        Goods goods = new Goods();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new Producer(goods));
        executor.execute(new Customer(goods));
        executor.shutdown();
    }

    static class Producer implements Runnable{

        private Goods goods = null;

        public Producer(Goods goods){
            this.goods = goods;
        }

        @Override
        public void run() {
            while (true){
                synchronized (goods){
                    //使用循环进行等待，相当于一个简单的自旋锁，防止虚假唤醒
                    while (goods.getTotal() > 5){
                        try {
                            System.out.println("商品数已达上限，等待消费");
                            goods.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    goods.add();
                    //notify和notifyAll在这里都可以使用，因为一共只有两个线程
                    goods.notifyAll();
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

        public Customer(Goods goods){
            this.goods = goods;
        }

        @Override
        public void run() {
            while (true){
                synchronized (goods){
                    while (goods.getTotal() < 4){
                        try {
                            System.out.println("商品已经快卖完，等待生产");
                            goods.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    goods.del();
                    goods.notifyAll();
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
