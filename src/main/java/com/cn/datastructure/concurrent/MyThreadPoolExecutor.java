package com.cn.datastructure.concurrent;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript: 自定义实现一个线程池
 * @date: 2019-07-17 15:46
 * <p>
 * 线程池提交任务时的核心执行流程：
 * 1、如果当前运行的线程少于corePoolSize，则创建新线程来执行任务（注意，执行这一步骤需要获取全局锁）。
 * 2、如果运行的线程等于或多于corePoolSize，则将任务加入BlockingQueue。
 * 3、如果无法将任务加入BlockingQueue（队列已满），则在非corePool中创建新的线程来处理任务（注意，执行这一步骤需要获取全局锁）。
 * 4、如果创建新线程将使当前运行的线程超出maximumPoolSize，任务将被拒绝，并调用RejectedExecutionHandler.rejectedExecution()方法。
 */
public class MyThreadPoolExecutor {


    /**
     * 核心的线程数量，当线程池中的线程数量小于该值时会直接创建新线程来执行任务
     */
    private volatile int corePoolSize;

    /**
     * 最大的线程数量
     */
    private volatile int maximumPoolSize;

    /**
     * 非核心线程的存活时间
     */
    private volatile long keepAliveTime;
    private TimeUnit unit;

    /**
     * 存放任务的阻塞队列
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 存放线程的线程池
     */
    private Set<Worker> workers;

    /**
     * 是否关闭线程池标志
     */
    private AtomicBoolean isShutDown = new AtomicBoolean(false);

    /**
     * 线程池中任务总数
     */
    private AtomicInteger totalTask = new AtomicInteger(0);

    private volatile ThreadFactory threadFactory;

    public ThreadFactory getThreadFactory(){
        return this.threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory){
        if (threadFactory == null) {
            throw new NullPointerException();
        }
        this.threadFactory = threadFactory;
    }

    /**
     * 执行工作器，这里运行一个事件loop,取得任务然后执行这些任务
     * @param worker
     */
    final void runWorker(Worker worker) {
        // 这个thread其实就是worker中的thread
        Thread thread = Thread.currentThread();
        Runnable task = worker.firstTask;
        worker.firstTask = null;
        //这里允许中断，所以设置下状态
        worker.unLock();

        try {
            while ( task != null || getTask() != null){
                worker.lock();

                //响应中断


                //执行任务

                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    worker.unLock();
                }
            }
        } finally {

        }


    }

    private Runnable getTask() {
        return null;
    }



    /**
     * 自定义一个工作器，该工作器主要职责是包装线程池中的线程，维护中断控制状态
     * Worker使用AQS实现一个简单的排它锁，免得再针对每个工作器创建一个锁，
     * 同时通过锁的状态也可以判断该工作器是否处于空闲状态
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable{
        //运行该工作器的线程
        private Thread thread;
        //创建该工作器时指定的第一个任务
        private Runnable firstTask;
        //线程任务计数器
        volatile long completedTasks;

        public Worker(Runnable task){
            setState(-1);
            this.firstTask = task;
            this.thread = getThreadFactory().newThread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        //region 锁相关
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 0;
        }
        @Override
        protected boolean tryAcquire(int arg) {
            if(compareAndSetState(0, 1)){
                this.setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock(){
            acquire(1);
        }
        public boolean tryLock(){
            return tryAcquire(1);
        }
        public void unLock(){
            release(0);
        }
        public boolean isLocked(){
            return isHeldExclusively();
        }
        //endregion

        //中断线程
        void interruptIfStarted(){
            Thread t = thread;
            // 这里如果工作器还没有开始执行任务，state是-1，此时就没必要进行中断了
            if(getState() >= 0 && t != null && !t.isInterrupted()){
                try {
                    t.interrupt();
                } catch (SecurityException e) {
                    //这里忽略该异常
                }
            }
        }
    }


    /**
     * 内部存放工作线程容器
     *
     * @param <T>
     */
    private final class ConcurrentHashSet<T> extends AbstractSet<T> {

        private final Object PRESENT = new Object();

        private ConcurrentHashMap<T, Object> map = new ConcurrentHashMap<>();

        @Override
        public Iterator<T> iterator() {
            return map.keySet().iterator();
        }

        @Override
        public boolean add(T t) {
            return map.put(t, PRESENT) == null;
        }

        @Override
        public boolean remove(Object o) {
            return map.remove(o) != null;
        }

        /**
         * ConcurrentHashMap的size计算在某些情况下并不准确，
         * 这里可能会有隐患
         * @return
         */
        @Override
        public int size() {
            return map.size();
        }

        public boolean contains(Object o) {
            return map.containsKey(o);
        }
    }

}
