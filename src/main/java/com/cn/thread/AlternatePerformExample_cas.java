package com.cn.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-09-16 14:10
 */
public class AlternatePerformExample_cas {

    public static void main(String[] args){
        int max = 10;
        ExecutorService executor = Executors.newFixedThreadPool(max);
        AtomicInteger state = new AtomicInteger(1);
        for (int i = 1; i <= max; i++) {
            executor.execute(new PrintTask(state, i, max));
        }
    }


    static class PrintTask implements Runnable{
        private int num;
        private int max;
        AtomicInteger state;

        PrintTask(AtomicInteger state, int num, int max){
            this.state = state;
            this.num = num;
            this.max = max;
        }

        private int getNextNum(){
            int nextNum = this.num + 1;
            if(nextNum > max){
                return 1;
            }else{
                return nextNum;
            }
        }

        @Override
        public void run() {
            while (true){
                while (this.state.compareAndSet(num, this.getNextNum())){
                    System.out.println("线程num:" + num);
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
