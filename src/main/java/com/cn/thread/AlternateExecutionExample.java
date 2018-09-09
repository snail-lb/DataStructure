package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 多个线程交替打印1,2,3,4.....
 * @date: 2018-09-09 12:39
 */
public class AlternateExecutionExample {
    public static void main(String[] args) {
        Integer threadNum = 5;

        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        Status status = new Status(threadNum);
        for (int i = 1; i <= threadNum; i++) {
            executor.execute(new ExeThread(i, status));
        }
    }

    static class ExeThread implements Runnable {

        private final Integer threadMark;

        private volatile Status status = null;

        public ExeThread(Integer num, Status status) {
            this.threadMark = num;
            this.status = status;
        }

        @Override
        public void run() {
            while (true) {
                if (!threadMark.equals(status.getMark())) {
                    continue;
                }

                System.out.println("线程" + threadMark + ": " + status.getCount());
                //这里同一时刻只有一个线程进行访问，所以不存在线程安全问题，不需要同步
                status.setValue();

                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Status{

        private Integer mark = 1;

        private Integer count = 1;

        private Integer maxMark;

        public Status(Integer maxMark) {
            this.maxMark = maxMark;
        }

        public Integer getMark() {
            return mark;
        }

        public Integer getCount() {
            return count;
        }

        public void setValue(){
            if(mark < maxMark) {
                mark++;
            }else{
                mark = 1;
            }
            count++;
        }
    }
}
