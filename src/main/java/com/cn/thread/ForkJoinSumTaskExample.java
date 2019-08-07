package com.cn.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-08-07 11:43
 */
public class ForkJoinSumTaskExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SumTask task = new SumTask(0, 100);
        ForkJoinPool pool = new ForkJoinPool();
        Future<Integer> result = pool.submit(task);
        Integer r = result.get();
        System.out.println(r);
    }

    private static class SumTask extends RecursiveTask<Integer> {
        private int start;
        private int end;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        protected Integer compute() {
            int sum = 0;
            if (start - end < 10) {
                for (int i = start; i < end; i++) {
                    sum += i;
                }
            } else {
                int middle = (start + end) / 2;
                SumTask left = new SumTask(start, middle);
                SumTask right = new SumTask(middle + 1, end);
                left.fork();
                right.fork();
                sum = left.join() + right.join();
            }
            return sum;
        }
    }
}
