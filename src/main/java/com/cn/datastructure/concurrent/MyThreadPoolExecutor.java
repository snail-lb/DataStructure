package com.cn.datastructure.concurrent;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.cn.datastructure.utils.MyRejectedExecutionHandler;

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


    // 线程池的状态
    //  RUNNING --shutdown()---> SHUTDOWN --队列为空而且线程池中执行的任务也为空---> TIDYING --terminated()执行完毕 --> TERMINATED
    //  RUNNING --shutdownNow()---> STOP --线程池中执行的任务为空---> TIDYING --terminated()执行完毕 --> TERMINATED
    private static final int RUNNING = 1;
    private static final int SHUTDOWN = 0;
    private static final int STOP = -1;
    private static final int TIDYING = -2;
    private static final int TERMINATED = -3;
    private final AtomicInteger state = new AtomicInteger(RUNNING);
    /**
     * 全局锁
     */
    private final ReentrantLock mainLock = new ReentrantLock();
    private final Condition termination = mainLock.newCondition();
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
    private TimeUnit timeUnit;
    /**
     * 存放任务的阻塞队列
     */
    private BlockingQueue<Runnable> workQueue;
    /**
     * 存放线程的线程池
     */
    private Set<Worker> workers = new ConcurrentHashSet<>();
    /**
     * 线程池中任务总数
     */
    private AtomicInteger totalTask = new AtomicInteger(0);
    /**
     * 线程工厂内
     */
    private volatile ThreadFactory threadFactory;
    /**
     * 任务提交拒绝策略
     */
    private volatile MyRejectedExecutionHandler handler;

    /**
     * 使用默认的线程工厂和拒绝策略创建一个线程池
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param timeUnit
     * @param workQueue
     */
    public MyThreadPoolExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime,
                                TimeUnit timeUnit,
                                BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue,
                Executors.defaultThreadFactory(), new MyThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 使用默认的拒绝策略创建一个线程工厂
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param timeUnit
     * @param workQueue
     * @param threadFactory
     */
    public MyThreadPoolExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime,
                                TimeUnit timeUnit,
                                BlockingQueue<Runnable> workQueue,
                                ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue,
                threadFactory, new MyThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 使用默认的线程工厂创建一个线程池
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param timeUnit
     * @param workQueue
     * @param handler
     */
    public MyThreadPoolExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime,
                                TimeUnit timeUnit,
                                BlockingQueue<Runnable> workQueue,
                                MyRejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue,
                Executors.defaultThreadFactory(), handler);
    }


    /**
     * 创建一个线程池
     * @param corePoolSize 核心线程大小
     * @param maximumPoolSize 线程池最大的线程黄素娘
     * @param keepAliveTime 非核心线程的存活时间
     * @param timeUnit 非核心线程的存活时间单位
     * @param workQueue 任务队列
     * @param threadFactory 线程工厂
     * @param handler 任务拒绝策略
     */
    public MyThreadPoolExecutor(int corePoolSize,
                                int maximumPoolSize,
                                long keepAliveTime,
                                TimeUnit timeUnit,
                                BlockingQueue<Runnable> workQueue,
                                ThreadFactory threadFactory,
                                MyRejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            keepAliveTime < 0 ||
            corePoolSize > maximumPoolSize) {
            throw new IllegalArgumentException();
        }
        if (timeUnit == null ||
                workQueue == null ||
                threadFactory == null ||
                handler == null) {
            throw new NullPointerException();
        }

        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    private int runStateOf(int runState) {
        return state.getAndSet(runState);
    }

    private boolean isRunning() {
        return state.get() >= RUNNING;
    }

    /**
     * 检查线程池是否已经关闭
     * 如果已经关闭进行相应的关闭操作
     */
    private void tryTerminate() {
        if (isRunning() || (state.get() == STOP && workQueue.size() > 0)) {
            return;
        }

        //走到这里就说明线程池已经被关闭了
        if (getTaskCount() != 0) {
            //还有任务在执行，这里就找到一个空闲的线程进行中断操作
            interruptIdleWorkers(true);
        }

        //走到这里就说明线程池已经被关闭，而且没有线程在执行任务了
        final ReentrantLock mainLock = this.mainLock;
        boolean lock = mainLock.tryLock();
        if (lock) {
            try {
                if (state.get() != TERMINATED) {
                    state.set(TIDYING);
                    try {
                        terminated();
                    } finally {
                        state.set(TERMINATED);
                        termination.signalAll();
                    }
                }
            } finally {
                mainLock.unlock();
            }
        }
    }

    /**
     * 中断没有任务的线程
     *
     * @param onlyOne
     */
    private void interruptIdleWorkers(boolean onlyOne) {
        for (Worker w : workers) {
            Thread t = w.thread;
            if (!t.isInterrupted() && w.tryLock()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                } finally {
                    w.unLock();
                }
            }
            if (onlyOne) {
                break;
            }
        }
    }

    /**
     * 中断所有线程
     */
    private void interruptWorkers() {
        for (Worker w : workers) {
            w.interruptIfStarted();
        }
    }

    /**
     * 获取任务队列中所有的任务
     *
     * @return
     */
    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> queue = workQueue;
        List<Runnable> taskList = new ArrayList<>();
        queue.drainTo(taskList);
        // 如果队列是一些DelayQueue之类的，drainTo可能无法完全清空队列，这里再手动删除一次
        if (!queue.isEmpty()) {
            for (Runnable r : queue.toArray(new Runnable[0])) {
                if (queue.remove(r))
                    taskList.add(r);
            }
        }
        return taskList;
    }

    /**
     * 添加一个工作执行器，并启动该线程
     *
     * @param command
     */
    private void addWorker(Runnable command, boolean core) {
        Worker worker = new Worker(command, core);
        workers.add(worker);
        //启动工作线程
        worker.startTask();
    }

    /**
     * 从任务队列中获取任务
     * 如果是核心线程来获取任务的话会一直阻塞等待任务队列，直到获取到任务
     * 如果是非核心线程来获取任务的话，就阻塞等待设定的存活时间大小，超时没有获取到就返回null
     * <p>
     * 返回null基本就意味着该工作器的任务执行完毕，其中的线程会执行完毕退出去
     *
     * @return
     */
    private Runnable getTask(boolean core) {
        Runnable task = null;
        try {
            if (core) {
                task = workQueue.take();
            } else {
                task = workQueue.poll(keepAliveTime, timeUnit);
            }
        } catch (InterruptedException e) {
            return null;
        }
        return task;
    }

    /**
     * 等待所有任务完成
     *
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            while (true) {
                if (state.get() == TERMINATED) {
                    return true;
                }
                if (nanos <= 0)
                    return false;
                nanos = termination.awaitNanos(nanos);
            }
        } finally {
            mainLock.unlock();
        }
    }

    public boolean isShutdown() {
        return state.get() <= SHUTDOWN;
    }

    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        if (threadFactory == null) {
            throw new NullPointerException();
        }
        this.threadFactory = threadFactory;
    }

    /**
     * 关闭线程池，不再接受新的任务，但是会等待任务队列中的任务执行完毕
     */
    public void shutdown() {
        state.set(SHUTDOWN);
        interruptIdleWorkers(false);
        onShutdown();
        tryTerminate();
    }

    /**
     * 关闭线程池，中断所有线程，任务队列中的任务也不会再执行
     */
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks;
        state.set(STOP);
        interruptWorkers();
        tasks = drainQueue();
        tryTerminate();
        return tasks;
    }

    public boolean isTerminated() {
        return state.get() == TERMINATED;
    }

    /**
     * 提交一个新任务
     *
     * @param command
     */
    public void execute(Runnable command) {
        if (null == command) {
            throw new NullPointerException("提交任务不能为空");
        }

        if (isShutdown()) {
            //线程池已经被关闭了，这里不再接收新的任务
            throw new IllegalStateException("线程池已关闭,不再接收新任务。");
        }

        totalTask.incrementAndGet();

        /*
        提交新任务核心逻辑
        1、如果当前运行的线程少于corePoolSize，则创建新线程来执行任务。
        2、如果运行的线程等于或多于corePoolSize，则将任务加入BlockingQueue。
        3、如果无法将任务加入BlockingQueue（队列已满），则在非corePool中创建新的线程来处理任务。
        4、如果创建新线程将使当前运行的线程超出maximumPoolSize，任务将被拒绝，并调用RejectedExecutionHandler.rejectedExecution()方法。
        */
        if (workers.size() < corePoolSize) {
            addWorker(command, true);
            return;
        }
        if (!workQueue.offer(command)) {
            if (workers.size() < maximumPoolSize) {
                addWorker(command, false);
            } else {
                reject(command);
            }
        }
    }

    /**
     * 返回完成执行的任务的大概总数
     *
     * @return
     */
    public long getCompletedTaskCount() {
        long completedTaskCount = 0;
        for (Worker worker : workers) {
            completedTaskCount += worker.completedTasks;
        }
        return completedTaskCount;
    }

    /**
     * 获取核心线程池大小
     * @return
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * 预先创建核心线程，该方法需要使用者手动调用
     *
     * @return
     */
    public boolean prestartAllCoreThreads() {
        while (workers.size() < corePoolSize) {
            addWorker(null, true);
        }
        return true;
    }

    /**
     * 默认的拒绝策略，抛出异常
     */
    public static class AbortPolicy implements MyRejectedExecutionHandler {

        public AbortPolicy() {
        }

        @Override
        public void rejectedExecution(Runnable r, MyThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }

    /**
     * 返回正在执行的任务的大概总数
     *
     * @return
     */
    public long getTaskCount() {
        long taskCount = 0;
        for (Worker worker : workers) {
            if (worker.isLocked()) {
                taskCount++;
            }
        }
        return taskCount;
    }

    final void reject(Runnable command) {
        handler.rejectedExecution(command, this);
    }

    /**
     * 执行工作器，这里运行一个事件loop,取得任务然后执行这些任务
     *
     * @param worker
     */
    final void runWorker(Worker worker) {
        // 这个thread其实就是worker中的thread
        Thread thread = Thread.currentThread();
        Runnable task = worker.firstTask;
        worker.firstTask = null;
        boolean core = worker.isCore();
        //这里允许中断，所以设置下状态
        worker.unLock();
        //用于记录当前线程中断的原因，如果是任务中发生了异常中断了，
        //那么就要在当前任务结束后重新添加一个执行器到线程池中，用来取代当前
        //这个被中断的执行器，如果是主动中断的就不用关心了
        boolean exceptionInterrupt = false;
        try {
            while (task != null || (task = getTask(core)) != null) {
                worker.lock();

                //如果线程池已被停止，就直接中断当前工作线程
                if (state.get() < SHUTDOWN && !thread.isInterrupted()) {
                    thread.interrupt();
                }

                //执行任务
                try {
                    beforeExecute(thread, task);
                    Throwable thrown = null;
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        thrown = e;
                        exceptionInterrupt = true;
                        throw e;
                    } catch (Error e) {
                        thrown = e;
                        exceptionInterrupt = true;
                        throw e;
                    } catch (Throwable e) {
                        thrown = e;
                        exceptionInterrupt = true;
                        throw new Error(e);
                    } finally {
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    totalTask.decrementAndGet();
                    worker.completedTasks++;
                    worker.unLock();
                }
            }
        } finally {
            // 释放当前工作器的线程
            workers.remove(worker);
            if (exceptionInterrupt && isRunning()) {
                addWorker(null, core);
            }
            tryTerminate();
        }
    }

    /**
     * 执行给定任务之前调用，可以记录日志或者记录一些其他操作
     * 默认不做任何操作
     *
     * @param thread
     * @param task
     */
    protected void beforeExecute(Thread thread, Runnable task) {
    }

    /**
     * 执行给定任务之后调用，可以进行一些异常处理（如果thrown!=null）
     * 默认不作任何操作
     *
     * @param task
     * @param thrown
     */
    protected void afterExecute(Runnable task, Throwable thrown) {
    }

    /**
     * 关闭线程池后执行
     */
    protected void terminated() {
    }

    /**
     * 关闭线程池时调用
     */
    protected void onShutdown() {

    }

    /**
     * 自定义一个工作器，该工作器主要职责是包装线程池中的线程，维护中断控制状态
     * Worker使用AQS实现一个简单的排它锁，免得再针对每个工作器创建一个锁，
     * 同时通过锁的状态也可以判断该工作器是否处于空闲状态
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
        //线程任务计数器
        volatile long completedTasks;
        //运行该工作器的线程
        private Thread thread;
        //创建该工作器时指定的第一个任务
        private Runnable firstTask;
        private boolean core;

        public Worker(Runnable task) {
            this(task, false);
        }

        public Worker(Runnable task, boolean core) {
            setState(-1);
            this.core = core;
            this.firstTask = task;
            this.thread = getThreadFactory().newThread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        public boolean isCore() {
            return core;
        }

        public void close() {
            if (thread != null && !thread.isInterrupted()) {
                thread.interrupt();
            }
        }

        public void startTask() {
            this.thread.start();
        }

        //region 锁相关
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 0;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
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

        public void lock() {
            acquire(1);
        }

        public boolean tryLock() {
            return tryAcquire(1);
        }

        public void unLock() {
            release(0);
        }

        public boolean isLocked() {
            return isHeldExclusively();
        }
        //endregion

        //中断线程
        void interruptIfStarted() {
            Thread t = thread;
            // 这里如果工作器还没有开始执行任务，state是-1，此时就没必要进行中断了
            if (getState() >= 0 && t != null && !t.isInterrupted()) {
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
         *
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
