package com.cn.datastructure.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript: 自定义实现一个阻塞队列
 * @date: 2019-07-05 15:17
 */
public class MyLinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, java.io.Serializable {

    /**
     * 队列容量
     */
    private final int capacity;
    /**
     * 队列元素个数
     */
    private final AtomicInteger count = new AtomicInteger();
    /**
     * 添加元素锁
     */
    private final Lock putLock = new ReentrantLock();
    /**
     * 移除元素锁
     */
    private final Lock takeLock = new ReentrantLock();
    /**
     * 存储头结点，  transient不被序列化
     */
    transient Node<E> head;
    /**
     * 用于还有剩余容量时的通知
     */
    private Condition notFull = putLock.newCondition();
    /**
     * 用于列表元素为空时的通知
     */
    private Condition notEmpty = takeLock.newCondition();
    /**
     * 尾节点
     */
    private transient Node<E> last;

    public MyLinkedBlockingQueue() {
        this.capacity = Integer.MAX_VALUE;
    }

    public MyLinkedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        this.capacity = capacity;
    }

    public MyLinkedBlockingQueue(Collection<? extends E> items) {
        this(Integer.MAX_VALUE);
        final Lock localPutLock = this.putLock;
        try {
            //这里不会发生竞争，主要使用可见性功能
            localPutLock.lock();
            int num = 0;
            for (E e : items) {
                if (e == null) {
                    throw new NullPointerException();
                }
                if (num == this.capacity) {
                    throw new IllegalStateException("队列已满");
                }
                this.enqueue(new Node<>(e));
                ++num;
            }
            this.count.set(num);
        } finally {
            localPutLock.unlock();
        }
    }

    /**
     * 在队尾添加元素
     *
     * @param node
     */
    private void enqueue(Node<E> node) {
        this.last = this.last.next = node;
    }

    /**
     * 从队列头部取出一个元素
     *
     * @return
     */
    private E dequeue() {
        final Node<E> h = this.head;
        this.head = h.next;
        h.next = null; // 帮助GC,减少引用链搜索？？？
        E item = h.item;
        h.item = null;
        return item;
    }

    /**
     * 通知put方法，可以插入元素了，一般在take/poll方法中调用。
     */
    private void signalNotFull() {
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * 通知take方法，有新元素了，一般在put/offer方法中调用。
     */
    private void signalNotEmpty() {
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }


    /**
     * 将指定元素插入到此队列的尾部（如果立即可行且不会超出此队列的容量），在成功时返回 true，如果此队列已满，则返回 false。
     * 当使用有容量限制的队列时，此方法通常要优于 add 方法，后者可能无法插入元素，而只是抛出一个异常。
     *
     * @param e
     * @return
     */
    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    /**
     * 将指定元素插入到此队列的尾部，如有必要，则等待空间变得可用.
     *
     * @param e
     * @throws InterruptedException
     */
    @Override
    public void put(E e) throws InterruptedException {
        if (null == e) {
            throw new NullPointerException();
        }

        final Lock putLock = this.putLock;
        final AtomicInteger count = this.count;
        Node<E> eNode = new Node<>(e);

        putLock.lockInterruptibly();
        try {
            while (count.get() == this.capacity) {
                this.notFull.await();
            }

            this.enqueue(eNode);
            this.count.getAndIncrement();
        } finally {
            putLock.unlock();
        }
        this.signalNotEmpty();
    }

    @Override
    public E take() throws InterruptedException {
        final Lock takeLock = this.takeLock;
        final AtomicInteger count = this.count;
        E e;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                this.notEmpty.await();
            }

            e = this.dequeue();
            this.count.getAndDecrement();
        } finally {
            takeLock.unlock();
        }
        this.signalNotFull();
        return e;
    }

    @Override
    public int remainingCapacity() {
        // 这里没有加锁，能得到正确结果吗？
        return capacity - this.count.get();
    }

    @Override
    public int size() {
        return this.count.get();
    }

    @Override
    public Iterator<E> iterator() {


        return null;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    /**
     * 删除指定数量元素，然后将c中元素添加到队列中
     *
     *
     * @param c
     * @param maxElements
     * @return
     */
    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (null == c) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0) {
            return 0;
        }

        // TODO 这里只用take锁可能会有丢数据的情况
        final Lock takeLock = this.takeLock;

        //找到需要移除的数据量
        int n = Math.min(maxElements, this.count.get());

        takeLock.lock();

        try {
            int i = 0;
            while (i < n) {
                //

                ++i;
            }

        } finally {

        }


        return 0;
    }

    /**
     * 内部使用的节点
     *
     * @param <E>
     */
    static class Node<E> {
        E item;
        Node<E> next;

        Node(E item) {
            this.item = item;
        }
    }
}
