package com.cn.datastructure;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import com.cn.datastructure.concurrent.MyLinkedBlockingQueue;

/**
 * 用生产者消费者模式简单测一下
 */
public class MyLinkedBlockingQueueTest2 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new MyLinkedBlockingQueue(20);
        for (int i = 0; i < 10; i++) {
            queue.put(i);
        }
        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            System.out.println(value);
            if(value == 7){
                iterator.remove();
            }
        }
        System.out.println(queue);
    }

}
