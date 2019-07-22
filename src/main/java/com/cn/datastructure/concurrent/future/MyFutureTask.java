package com.cn.datastructure.concurrent.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-07-19 16:09
 */
public class MyFutureTask<T> implements RunnableFuture {

    /**
     * 保存的callable对象
     */
    private Callable<T> callable;
    /**
     * callable 执行结果
     */
    private T result;
    /**
     * 使用锁
     */
    private ReentrantLock mainLock = new ReentrantLock();
    private Condition mainCondition = mainLock.newCondition();
    /**
     * 执行当前任务的线程对象
     */
    private Thread runner;
    /**
     * 记录异常
     */
    private Throwable throwable = null;
    /**
     * 记录状态
     */
    private AtomicInteger state;
    private static final int NEW          = -1;
    private static final int RUNNING      = 0;
    private static final int COMPLETING   = 1;
    private static final int NORMAL       = 2;
    private static final int EXCEPTIONAL  = 3;
    private static final int CANCELLED    = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED  = 6;


    public MyFutureTask(Callable<T> callable) {
        if(null == callable){
            throw new NullPointerException();
        }
        this.callable = callable;
        this.state = new AtomicInteger(NEW);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        int s = state.get();
        if (s > RUNNING){
            //已经运行结束了
            return false;
        } else if (s == NEW){
            if (state.compareAndSet(NEW, CANCELLED)){
                return true;
            }
        }

        //正在运行中，直接中断线程
        if(mayInterruptIfRunning) {
            if (runner != null && !runner.isInterrupted()) {
                runner.interrupt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCancelled() {
        return state.get() >= CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state.get() != NEW;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        int s = state.get();
        if (s <= COMPLETING)
            s = awaitDone(false, 0L);
        return report(s);
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null) {
            throw new NullPointerException();
        }
        int s = awaitDone(true, unit.toNanos(timeout));
        if(s <= RUNNING){
            throw new TimeoutException();
        }
        return report(s);

    }

    /**
     * 等待任务完成
     * @param isTimeout 是否设置超时等待
     * @param nanos 超时等待的时间，如果没有设置超时该值将失效
     * @return
     * @throws InterruptedException
     */
    private int awaitDone(boolean isTimeout, long nanos) throws InterruptedException{
        int s = state.get();
        if (s > COMPLETING){
            //这里就说明已经跑完了，而且没有异常，返回结果就行了
            return s;
        }
        if(!isTimeout){
            nanos = Long.MIN_VALUE;
        }

        mainLock.lock();
        try {
            while (true) {
                if(runner != null && runner.isInterrupted()){
                    throw new InterruptedException();
                }

                s = state.get();
                if (s > COMPLETING){
                    break;
                }
                if (isTimeout && nanos <= 0L) {
                    return s;
                }

                long n = mainCondition.awaitNanos(nanos);
                if(isTimeout) {
                    nanos = n;
                }
            }
            //这里就是跑完了
            return s;
        } finally {
            mainLock.unlock();
        }
    }

    private T report(int s) throws ExecutionException {
        T x = result;
        if (s == NORMAL)
            return x;
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException(throwable);
    }

    @Override
    public void run() {
        long s = state.get();
        //任务已经被取消了
        if(s == CANCELLED){
            throw new CancellationException();
        }
        // 给任务已经启动，不再重复启动
        if(s > NEW){
            return;
        }

        //CAS设置状态，如果失败说明有其他线程已经启动了任务，就不重复启动了
        if (!state.compareAndSet(NEW, RUNNING)){
            return;
        }

        try {
            //获取到当前执行任务的线程
            runner = Thread.currentThread();
            Callable<T> c = callable;
            if(null != c && state.get()==RUNNING){
                T re = null;
                boolean ran;
                try {
                    re = c.call();
                    ran = true;
                    state.compareAndSet(RUNNING, COMPLETING);
                } catch (Exception e) {
                    ran = false;
                    throwable = e;
                    state.compareAndSet(RUNNING, EXCEPTIONAL);
                }
                if (ran){
                    result = re;
                    state.compareAndSet(COMPLETING, NORMAL);
                }
            }
        } finally {
            runner = null;
            mainLock.lock();
            try {
                mainCondition.signalAll();
            } finally {
                mainLock.unlock();
            }
        }
    }
}
