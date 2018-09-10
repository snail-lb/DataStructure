package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: n个线程交替打印1,2,3,4,5,6,...  使用wait完成
 * @date: 2018-09-10 09:47
 */
public class AlternatePerformExample_wait {

    public static void main(String[] args){
        Integer threadNum = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        Status status = new Status(threadNum);
        for (int i = 1; i <= threadNum; i++) {
            executor.execute(new PerformThread(status, i));
        }
        executor.shutdown();
    }

    static class PerformThread implements Runnable{

        //status的写操作在同步块中，所以这里就不需要volatile关键字来保证可见性了
        private Status status = null;

        private final Integer threadNum;

        public PerformThread(Status status, Integer threadNum) {
            this.status = status;
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            while (true){
                if(status.getThreadMark().equals(threadNum)){
                    synchronized (status) {
                        //执行
                        System.out.println("线程" + threadNum + ": " + status.getNum());
                        status.setValue();
                        status.notifyAll();
                    }
                }else{
                    synchronized (status){
                        try {
                            //只有符合条件的才能跳出等待
                            while(!status.getThreadMark().equals(threadNum)) {
                                status.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Status{
        private Integer num = 1;
        private Integer threadMark = 1;
        private Integer threadNum;

        public Status(Integer threadNum) {
            this.threadNum = threadNum;
        }

        public Integer getNum() {
            return num;
        }

        public Integer getThreadMark() {
            return threadMark;
        }

        public void setValue(){
            if(threadMark < threadNum){
                threadMark++;
            }else{
                threadMark = 1;
            }
            num++;
        }
    }
}
