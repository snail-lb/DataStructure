package com.cn.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-07-02 09:32
 */
public class CountDownLatchExample {
    static CountDownLatch cdl = new CountDownLatch(2);

    public static void main(String[] args){
        Thread thrad = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1");
            cdl.countDown();
            System.out.println("2");
            cdl.countDown();
        });
        thrad.start();

        try {
            System.out.println("等待工作完成");
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("工作完成");

    }

}
