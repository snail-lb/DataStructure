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
public class ForkJoinFindTaskExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int size = 10000;
        int[] findArray = new int[size];
        for (int i = 0; i < size; i++) {
            findArray[i] = (int) (Math.random() * 10000);
        }
        long start = System.currentTimeMillis();
        FindTask task = new FindTask(findArray, 0, size-1);
        ForkJoinPool pool = new ForkJoinPool();
        Future<Integer> result = pool.submit(task);
        Integer r = result.get();
        System.out.println(r + " 耗时：" + (System.currentTimeMillis()-start));
    }

    private static class FindTask extends RecursiveTask<Integer> {
        private int start;
        private int end;
        private int[] find;

        FindTask(int[] find, int start, int end) {
            this.find = find;
            this.start = start;
            this.end = end;
        }

        protected Integer compute() {
            int max;
            if (end - start < 100) {
                max = find[start];
                for (int i = start + 1; i <= end; i++) {
                    int value = find[i];
//                    System.out.println(i + " : " + value);
                    if (max < value) {
                        max = value;
                    }
                }
            } else {
                int middle = (start + end) / 2;
                FindTask left = new FindTask(this.find, start, middle);
                FindTask right = new FindTask(this.find, middle + 1, end);
                left.fork();
                right.fork();
                int maxLeft = left.join();
                int maxRight = right.join();
                max = maxLeft > maxRight ? maxLeft : maxRight;
            }
            return max;
        }
    }
}
