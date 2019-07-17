package com.cn.datastructure.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
 *
 * 实现基本原理是使用通知模式实现的：
 * 所谓通知模式，就是当生产者往满的队列里添加元素时会阻塞住生产者，
 * 当消费者消费了一个队列中的元素后，会通知生产者当前队列可用。
 *
 * 基本实现就是使用两个锁 putLock 和takeLock,  putLock--->notFull  takeLock--->notEmpty
 * put元素时如果队列已达最大，使用notFull.wait()进入循环阻塞，直到被唤醒，添加元素完成后调用notEmpty.notify()
 * take元素同理
 *
 *
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
        this(Integer.MAX_VALUE);
    }

    public MyLinkedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        this.capacity = capacity;
        // 第一个节点永远都是空的
        last = head = new Node<E>(null);
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
        E item = this.head.item;
        this.head.item = null;
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

    /**
     * 获取到put和take的锁，这里最好独立出来，所有需要同时获取两个锁的位置都使用该方法
     * 如果分开写，有可能没有按照都先获取一个锁，在获取另一个锁的顺序，这样容易形成死锁
     * <p>
     * 比如一个线程 是 put.lock;take.lock
     * 另一个线程是  take.lock;put.lock
     * 两个同时将入都先获取了第一个锁，然后获取第二个锁的时候就会形成死锁
     */
    private void fullyLock() {
        putLock.lock();
        takeLock.lock();
    }

    /**
     * 释放两个锁
     * 释放顺序不能变
     */
    private void fullyUnLock() {
        takeLock.unlock();
        putLock.unlock();
    }

    /**
     * --> trail --> p -->  移除p节点
     *
     * @param p
     * @param trail
     */
    private void unLink(Node<E> p, Node<E> trail) {
        p.item = null;
        trail.next = p.next;
        if (p == last) {
            last = trail;
        }
        if (this.count.getAndDecrement() == this.capacity) {
            this.signalNotFull();
        }
    }

    @Override
    public boolean remove(Object o) {
        if (null == o || this.count.get() == 0) {
            return false;
        }

        this.fullyLock();
        try {
            // 这里遍历整个队列，找有没有和o相等的对象
            Node<E> trail = this.head;
            Node<E> p = trail.next;
            while (p != null) {
                if (o.equals(p.item)) {
                    this.unLink(p, trail);
                    return true;
                }
                trail = p;
                p = p.next;
            }
            return false;
        } finally {
            this.fullyUnLock();
        }
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
        if (null == e) {
            throw new NullPointerException();
        }

        if (this.count.get() == this.capacity) {
            return false;
        }

        boolean signalNotEmpty = false;
        final Lock putLock = this.putLock;
        putLock.lock();
        try {
            if (this.count.get() == this.capacity) {
                return false;
            }

            this.enqueue(new Node<>(e));
            this.count.getAndIncrement();
            signalNotEmpty = true;
            return true;
        } finally {
            putLock.unlock();
            if (signalNotEmpty) {
                this.signalNotEmpty();
            }
        }
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (null == e) {
            throw new NullPointerException();
        }

        if (this.count.get() == this.capacity) {
            return false;
        }

        boolean signalNotEmpty = false;
        long nanos = unit.toNanos(timeout);
        final Lock putLock = this.putLock;
        putLock.lock();
        try {
            while (this.count.get() == this.capacity) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            }

            this.enqueue(new Node<>(e));
            this.count.getAndIncrement();
            signalNotEmpty = true;
            return true;
        } finally {
            putLock.unlock();
            if (signalNotEmpty) {
                this.signalNotEmpty();
            }
        }
    }

    @Override
    public E poll() {
        if (this.count.get() == 0) {
            return null;
        }

        final Lock tackLock = this.takeLock;
        boolean signalNotFull = false;

        tackLock.lock();
        try {
            if (this.count.get() != 0) {
                E e = this.dequeue();
                this.count.getAndDecrement();
                signalNotFull = true;
                return e;
            }
            return null;
        } finally {
            tackLock.unlock();
            if (signalNotFull) {
                this.signalNotFull();
            }
        }
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        if (this.count.get() == 0) {
            return null;
        }

        final Lock tackLock = this.takeLock;
        boolean signalNotFull = false;
        long nanos = unit.toNanos(timeout);

        tackLock.lock();
        try {
            while (this.count.get() == 0) {
                if (nanos <= 0) {
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            }

            if (this.count.get() != 0) {
                E e = this.dequeue();
                signalNotFull = true;
                int c = this.count.getAndDecrement();
                // 这个操作是必须的吗?  因为每添加一个元素都会进行通知，所以这里应该没必要进行通知了吧？
//                if (c > 1) {
//                    notEmpty.signal();
//                }
                return e;
            }
            return null;
        } finally {
            tackLock.unlock();
            if (signalNotFull) {
                this.signalNotFull();
            }
        }
    }

    @Override
    public E peek() {
        if (this.count.get() == 0) {
            return null;
        }

        final Lock tackLock = this.takeLock;
        tackLock.lock();
        try {
            Node<E> eNode = this.head.next;
            if (null == eNode) {
                return null;
            } else {
                return eNode.item;
            }
        } finally {
            tackLock.unlock();
        }
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
        return new InnerIterator();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return this.drainTo(c, Integer.MAX_VALUE);
    }

    /**
     * 删除指定数量元素，然后将c中元素添加到队列中
     * 将指定数量的元素出队列，放到指定的Collection中
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

        final Lock takeLock = this.takeLock;
        boolean signalNotFull = false;

        takeLock.lock();
        try {
            int i = 0;
            //找到需要移除的数据量
            int n = Math.min(maxElements, this.count.get());
            Node<E> h;
            h = head;
            try {
                while (i < n) {
                    Node<E> eNode = h.next;
                    c.add(eNode.item);
                    eNode.item = null;
                    h.next = null;
                    h = eNode;
                    ++i;
                }
                return n;
            } finally {
                //这一步必须执行，否则会有问题
                if (i > 0) {
                    this.head = h;
                    signalNotFull = (this.count.addAndGet(-i) == this.capacity);
                }
            }
        } finally {
            takeLock.unlock();
            if (signalNotFull) {
                this.signalNotFull();
            }
        }
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


    private class InnerIterator implements Iterator<E> {
        private Node<E> current;
        private Node<E> lastRet;
        private E currentElement;

        InnerIterator() {
            fullyLock();

            try {
                this.current = head.next;
                if (this.current != null) {
                    this.currentElement = this.current.item;
                }
            } finally {
                fullyUnLock();
            }

        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        private Node<E> nextNode(Node<E> node) {
            while (true) {
                Node nextNode = node.next;
                if (nextNode == node) {
                    return head.next;
                }

                if (nextNode == null || nextNode.item != null) {
                    return nextNode;
                }

                node = nextNode;
            }
        }

        @Override
        public E next() {
            fullyLock();
            try {
                if (this.current == null) {
                    throw new NoSuchElementException();
                }

                E node = this.currentElement;
                this.lastRet = this.current;
                this.current = this.nextNode(this.current);
                this.currentElement = this.current == null ? null : this.current.item;
                return node;
            } finally {
                fullyUnLock();
            }
        }

        @Override
        public void remove() {
            if (this.lastRet == null) {
                throw new IllegalStateException();
            }

            fullyLock();

            try {
                Node ret = this.lastRet;
                this.lastRet = null;
                Node trail = head;

                for (Node p = trail.next; p != null; p = p.next) {
                    if (p == ret) {
                        unLink(p, trail);
                        break;
                    }
                    trail = p;
                }
            } finally {
                fullyUnLock();
            }
        }
    }

}
