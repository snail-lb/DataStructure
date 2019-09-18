package com.cn.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-07-02 09:32
 * 计数器，达到指定数量之后阻塞才会被释放
 *
 * 理解：
 * 我们的Object中有wait和notify、notyfyAll
 * 1. 现在有需求就是有一个wait需要被唤醒，那么我们可以用notify来完成。
 * 2. 现在有一个新需求是要求多个wait需要被唤醒，那么我们可以用notifyAll来完成。
 * 3. 现在又有一个新需求了，要求多个条件满足之后，一个wait然后被唤醒，那么这是就可以用CountDownLatch来完成。
 *
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
