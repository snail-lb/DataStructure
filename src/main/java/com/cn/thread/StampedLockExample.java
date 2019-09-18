package com.cn.thread;

import java.util.concurrent.locks.StampedLock;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-09-16 17:14
 * StampedLock是从JDK1.8开始引入的，它的出现对于ReentrantReadWriteLock在读多写少的情况下的效率问题还有写线程容易产生“饥饿”的问题有了很大的解决。
 * ReentrantReadWriteLock 在沒有任何读写锁时才能获取到写锁，
 * StampedLock认为尝试获取读锁的时候如果有写锁存在不应该是阻塞而是重读，而且在StampedLock的乐观锁的情况下写锁可以直接“闯入”。
 * StampedLock使用了一个stamp的概念（可以就理解为时间戳），它可以被用作加锁解锁操作的一个票据。
 */
public class StampedLockExample {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    /**
     * 独占式写锁
     * @param deltaX
     * @param deltaY
     */
    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    /**
     * 乐观读锁访问共享资源
     * 注意：乐观读锁在保证数据一致性上需要拷贝一份要操作的变量到方法栈，并且在操作数据时候可能其他写线程已经修改了数据，
     * 而我们操作的是方法栈里面的数据，也就是一个快照，所以最多返回的不是最新的数据，但是一致性还是得到保障的。
     *
     * @return
     */
    double distanceFromOrigin() {
        //乐观读
        long stamp = sl.tryOptimisticRead();
        //读到内容到副本
        double currentX = x, currentY = y;
        //调用validate来验证（如果被其他线程修改过了那么stamp就变了将返回false）
        if (!sl.validate(stamp)) {
            //验证失败了就用读锁（将阻塞写操作）
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                //释放读锁
                sl.unlockRead(stamp);
            }
        }
        //验证成功直接使用变量值
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    /**
     * 悲观读锁， 锁升级
     * @param newX
     * @param newY
     */
    void moveIfAtOrigin(double newX, double newY) {
        //获取读锁
        long stamp = sl.readLock();
        try {
            //判断某种条件，需要进行写的操作
            while (x == 0.0 && y == 0.0) {
                //升级为写锁
                long ws = sl.tryConvertToWriteLock(stamp);
                //如果ws不为零表示升级成功并获取到写锁
                if (ws != 0L) {
                    //如果成功 替换票据
                    stamp = ws;
                    // 状态改变
                    x = newX;
                    y = newY;
                    break;
                }else {
                    //升级失败释放读锁并直接使用写锁
                    //我们显式释放读锁
                    sl.unlockRead(stamp);
                    //显式直接进行写锁 然后再通过循环再试
                    stamp = sl.writeLock();
                }
            }
        } finally {
            //释放
            sl.unlock(stamp);
        }
    }
}
