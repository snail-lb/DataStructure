package com.cn.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-07-02 22:23
 * Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源。
 * 理解：
 *  有一个需求，要求一段代码最多只能有一个线程同时进入执行，这个时候可以用锁来实现了。
 *  那么如果现在有一个需求，要求一段代码最多可以有N个线程同时进入执行，那么这个时候就用上信号量了。
 *  那么现在又有另一个需求，就是要求一段代码至少要有N个线程同时进入执行，那么这个时候就可以用栅栏进行完成了。
 */
public class SemaphoreExample {
    /**
     * 共设置两个许可
     */
    static Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args){
        for (int i = 0; i < 6; i++) {
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
                // 获取许可， 这里同时一共只会有两个线程拥有许可，其他线程会阻塞
                semaphore.acquire();
                TimeUnit.MILLISECONDS.sleep((long) (Math.random()*1000));
                System.out.println(num + "开始工作,剩余排队数" + semaphore.getQueueLength());
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }
    }
}
