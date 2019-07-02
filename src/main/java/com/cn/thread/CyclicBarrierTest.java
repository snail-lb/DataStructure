package com.cn.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-07-02 22:05
 *
 * CyclicBarrier让一组线程到达一个屏障（也可以叫同步点）时被阻塞，
 * 直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续运行。
 */
public class CyclicBarrierTest {

    static Integer cbNum = 5;

    static CyclicBarrier cb = new CyclicBarrier(cbNum, () -> {
        System.out.println("阶段性工作都完成，进入下一阶段继续工作");
    });

    public static void main(String[] args){
        for (int i = 0; i < cbNum; i++) {
            new Thread(new Work(i)).start();
        }
    }

    static class Work implements Runnable{
        Integer num = 0;
        Work(Integer num){
            this.num = num;
        }
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1L);
                System.out.println("开始工作：" + this.num);
                cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(this.num + "...");
        }
    }
}
