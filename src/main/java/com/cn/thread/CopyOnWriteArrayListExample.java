package com.cn.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * CopyOnWriteArrayList :线程安全且读操作无锁的ArrayList，写操作则通过创建底层数组的新副本来实现，
 * 是一种读写分离的并发策略，我们也可以称这种容器为"写时复制器"，
 * 允许并发读，读操作是无锁的，性能较高。至于写操作，比如向容器中添加一个元素，则首先将当前容器复制一份，
 * 然后在新副本上执行写操作，结束之后再将原容器的引用指向新容器。
 *
 * 优点：适用于读多写少的并发场景。
 *
 * 缺点：一是内存占用问题，毕竟每次执行写操作都要将原容器拷贝一份，数据量大时，对内存压力较大，可能会引起频繁GC；
 * 二是无法保证实时性，Vector对于读写操作均加锁同步，可以保证读和写的强一致性。
 *
 * 添加时最好批量添加
 *
 * Vector虽然是线程安全的，但是只是一种相对的线程安全而不是绝对的线程安全，它只能够保证增、删、改、查的单个操作一定是原子的，不会被打断，但是如果组合起来用，并不能保证线程安全性。
 * 比如就一个线程1在遍历一个Vector中的元素、线程2在删除一个Vector中的元素一样，势必产生并发修改异常，也就是fail-fast。
 * 一般用于读多写少的并发环境
 * @date: 2018-09-16 14:42
 */
public class CopyOnWriteArrayListExample {

    public static void main(String[] args){
        CopyOnWriteArrayList coal = new CopyOnWriteArrayList();

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        coal.addAll(list);
    }
}
