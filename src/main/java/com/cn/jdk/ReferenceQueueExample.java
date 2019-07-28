package com.cn.jdk;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-07-28 11:06
 * 其他类型应用的简单使用。
 *
 * 在java的引用体系中，存在着强引用，软引用，弱引用，虚引用，这4种引用类型。在正常的使用过程中，我们定义的类型都是强引用的，
 * 这种引用类型在回收中，只有当其它对象没有对这个对象的引用时，才会被GC回收掉。简单来说，对于以下定义：
 *
 * Object obj = new Object();
 * Ref ref = new Ref(obj);
 *
 * 在这种情况下，如果ref没有被GC，那么obj这个对象肯定不会GC的。因为ref引用到了obj。如果obj是一个大对象呢，多个这种对象的话，应用肯定一会就挂掉了。
 * 那么，如果我们希望在这个体系中，如果obj没有被其它对象引用，只是在这个Ref中存在引用时，就把obj对象gc掉。这时候就可以使用这里提到的Reference对象了。
 *
 * 我们希望当一个对象被gc掉的时候通知用户线程，进行额外的处理时，就需要使用引用队列了。ReferenceQueue即这样的一个对象，当一个obj被gc掉之后，
 * 其相应的包装类，即ref对象会被放入queue中。我们可以从queue中获取到相应的对象信息，同时进行额外的处理。比如反向操作，数据清理等。
 *
 *在google guava中，核心原理和本例比较类似，通过软引用或虚引用实现一些淘汰策略。
 * 在guava中不需要单开线程来处理队列，通过继承一个指定的对象(可以理解为weakReference和callback的组合对象)，
 * 当value值被gc之后，即可以直接在回调中处理业务即可，不需要自己来监控queue。（见FinalizableReference)
 */
public class ReferenceQueueExample {

    private static Map<Object, WeakR> map = new HashMap<>();

    private static final int length = 1024 * 1024;

    /**
     * 引用队列
     */
    private static ReferenceQueue referenceQueue = new ReferenceQueue();

    public static void main(String[] args){
        Thread thread = new Thread(new ReferenceQueueListener());
        thread.start();
        putValue();
    }

    public static void putValue(){
        for(int i = 0;i < 10000;i++) {
            byte[] bytesKey = new byte[length];
            byte[] bytesValue = new byte[length];
            map.put(bytesKey, new WeakR(bytesKey, bytesValue, referenceQueue));
        }
    }

    /**
     * 创建一个软应用类，描述一种强key关系的处理，当value值被回收之后，我们可以通过反向引用将key从map中移除的做法
     * 即通过在weakReference中加入其所引用的key值，以获取key信息，再反向移除map信息
     */
    static class WeakR extends WeakReference<byte[]> {
        private Object key;
        WeakR(Object key, byte[] referent, ReferenceQueue<? super byte[]> q) {
            super(referent, q);
            this.key = key;
        }
    }

    /**
     * 引用队列监听线程
     */
    static class ReferenceQueueListener implements Runnable{
        @Override
        public void run() {
            int cnt = 0;
            WeakR k;
            try {
                while((k = (WeakR) referenceQueue.remove()) != null) {
                    System.out.println((cnt++) + "回收了:" + k);
                    //触发反向hash remove
                    map.remove(k.key);
                    //额外对key对象作其它处理，比如关闭流，通知操作等
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
