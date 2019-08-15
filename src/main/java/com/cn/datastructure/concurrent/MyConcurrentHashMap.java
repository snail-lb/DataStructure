package com.cn.datastructure.concurrent;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 利用MyConcurrentHashMap实现一个线程安全的Map,内部核心同步原理参照ConcurrentHashMap源码
 * 主要保证Map的put，get，remove操作的线程安全，且保证扩容时这些操作也是线程安全的
 * put、reomve的安全通过锁来实现，get方法不会被阻塞，在扩容时也可正常使用，基本原理是在get时
 * 如果key的hash槽数据还没有被迁移，那么就直接从该槽中的数据进行读取，如果已经被迁移到新的
 * 数组上了，那就从新数组上去读取key的值。
 * ConcurrentHashMap的扩容操作是线程安全的，而且支持同时多个线程同步进行扩容操作，扩容迁移数据时通过锁定头结点来保证线程安全，
 * 多个线程可以同步迁移不同槽点的数据。
 * <p>
 * ConcurrentHashMap的负载因子只在初始化计算数组长度是起作用，之后就不会使用该负载因子了，之后的计算方式是： table.size*2 - table.size/2
 * ConcurrentHashMap在链表长度超过8时会转化为红黑树，在本例中暂时不做此优化。
 *
 * @param <K>
 * @param <V>
 */
public class MyConcurrentHashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    /**
     * 默认的初始化容量，这个容量指定的数组的初始化默认长度
     */
    transient final static Integer DEFAULT_MIN_CAPACITY = 16;
    /**
     * map最大容量,这个容量指定的数组的最大长度
     */
    transient final static Integer DEFAULT_MAX_CAPACITY = 1 << 30;
    /**
     * 默认的负载因子，就是如果map中元素个数超过了threshold，就进行扩容
     * 这个值如果比较大，那么给table扩容的可能性就会降低，但是每条node链上的元素就会较多，占用的内存就比较小，但是查询时间就会变长，
     * 这个值如果比较小，那扩容的可能性就会增大，占用内存会更大，node链上的元素就会少一些，查询时间会更短
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /*
     * Encodings for Node hash fields. See above for explanation.
     */
    static final int MOVED = -1; // hash for forwarding nodes
    //    static final int TREEBIN   = -2; // hash for roots of trees
    //    static final int RESERVED  = -3; // hash for transient reservations
    // TODO 这里暂时没太明白计算hash为什么需要和这个值进行计算
    static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
    private static final sun.misc.Unsafe U;
    private static final long ABASE;
    private static final int ASHIFT;

    /**
     * 当前CPU数量，限制扩容线程数量
     */
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    /**
     * The number of bits used for generation stamp in sizeCtl.
     * Must be at least 6 for 32bit arrays.
     */
    private static int RESIZE_STAMP_BITS = 16;

    /**
     * The bit shift for recording size stamp in sizeCtl.
     */
    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;


    /**
     * 帮助扩容的最大线程数
     * The maximum number of threads that can help resize.
     * Must fit in 32 - RESIZE_STAMP_BITS bits.
     */
    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;

    /**
     * 扩容时每次任务迁移的最小的槽数
     */
    private static final int MIN_TRANSFER_STRIDE = 16;

    static {
        try {
            U = sun.misc.Unsafe.getUnsafe();
            Class<?> ak = Node[].class;
            ABASE = U.arrayBaseOffset(ak);
            int scale = U.arrayIndexScale(ak);
            if ((scale & (scale - 1)) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * 节点数组,长度总是2的n次方
     */
    transient volatile Node<K, V>[] table;
    /**
     * 扩容时的新数组，只有在扩容的时候才不为空
     */
    transient volatile Node<K, V>[] newTable;
    transient Set<Entry<K, V>> entrySet;
    /**
     * 扩容时下一个需要分割的索引位置
     * The next table index (plus one) to split while resizing.
     */
    private transient volatile AtomicInteger transferIndex = new AtomicInteger(-1);
    /**
     * 当前ConcurrentHashMap的基本大小，
     * 这里纪录的只是一个基本值，一个线程put完成之后会对该值进行CAS操作，如果CAS失败的话，该值纪录数会小于实际大小
     */
    private transient AtomicLong baseCount = new AtomicLong(0);
    /**
     * 纪录当前ConcurrentHashMap的状态
     * sizeCtl<-1 表示当前正在有 -sizeCtl-1个线程正在进行扩容操作
     * sizeCtl==-1 表示当前正在进行初始化操作
     * sizeCtl=0  表示没有指定初始容量
     * sizeCtl>0  在初始化之前表示初始容量，在初始化之后表示需要扩容的阈值，
     * 除初始阈值指定负载因子外，其他自动计算扩容时计算阈值的方式：table.size*2 - table.size/2
     */
    private transient volatile AtomicInteger sizeCtl = new AtomicInteger(0);

    //    final float loadFactor = 0.75f;
    public MyConcurrentHashMap() {
        sizeCtl.set(DEFAULT_MIN_CAPACITY);
    }

    /**
     * 创建一个制定初始容量大小的map
     *
     * @param initialCapacity 初始容量大小， 程序会根据该值计算出基于该大小的最小table长度
     */
    public MyConcurrentHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * @param initialCapacity
     * @param loadFactor
     */
    public MyConcurrentHashMap(int initialCapacity, final float loadFactor) {
        if (initialCapacity < 0 || loadFactor < 0 || loadFactor > 1) {
            throw new IllegalArgumentException();
        }
        //这里+1是向上取整
        long size = (long) ((long) initialCapacity / loadFactor + 1.0f);
        int cap = tableSizeFor(size);
        sizeCtl.set(cap);
    }

    public MyConcurrentHashMap(Map<? extends K, ? extends V> m) {
        sizeCtl.set(DEFAULT_MIN_CAPACITY);
        putAll(m);
    }

    /**
     * 计算cap个节点时，table的大小
     * 满足 2^n >= cap 这个条件的n的最小值
     *
     * @param cap
     * @return
     */
    static final int tableSizeFor(long cap) {
        if (cap < 0) {
            throw new IllegalArgumentException("Illegal initial cap: " + cap);
        }
        int n = 1, size = 1;
        while (size < cap) {
            n++;
            size = 1 << n;
        }
        return size > DEFAULT_MAX_CAPACITY ? DEFAULT_MAX_CAPACITY : size;
    }

    /**
     * volatile获取某个位置的值
     *
     * @param tab
     * @param i
     * @param <K>
     * @param <V>
     * @return
     */
    static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
        return (Node<K, V>) U.getObjectVolatile(tab, byteOffset(i));
    }

    /**
     * CAS方式设置数组中的某个值
     *
     * @param tab
     * @param i
     * @param expect
     * @param update
     * @param <K>
     * @param <V>
     * @return
     */
    static final <K, V> boolean casTabAt(Node<K, V>[] tab, int i, Node<K, V> expect, Node<K, V> update) {
        return U.compareAndSwapObject(tab, byteOffset(i), expect, update);
    }

    /**
     * 修改数组中的某个值
     *
     * @param tab
     * @param i
     * @param update
     * @param <K>
     * @param <V>
     */
    static final <K, V> void setTabAt(Node<K, V>[] tab, int i, Node<K, V> update) {
        U.putObjectVolatile(tab, byteOffset(i), update);
    }

    /**
     * 计算偏移量
     *
     * @param i
     * @return
     */
    static final long byteOffset(int i) {
        return ((long) i << ASHIFT) + ABASE;
    }

    private Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    /**
     * 公共的替换节点方法
     * 如果cv不为空且与key值对应的value相匹配，就用value替换key的value
     * 如果value为null，则删除key
     *
     * @param key
     * @param value
     * @param cv
     * @return
     */
    final V replaceNode(Object key, V value, Object cv) {
        int hash = spread(key.hashCode());
        for (Node<K, V>[] tab = table; ; ) {
            Node<K, V> first;
            int n, i, fh;
            if (tab == null || (n = tab.length) == 0 ||
                    (first = tabAt(tab, i = (n - 1) & hash)) == null) {
                // 说明还没有初始化或者该槽中还没有任何值
                break;
            } else if ((fh = first.hash) == MOVED) {
                //说明正在扩容
                tab = helpTransfer(tab, first);
            } else {
                V oldValue = null;
                boolean validated = false;
                synchronized (first) {
                    //扩容之后，first有可能就不再试first了，所以这里需要再次验证下
                    if (tabAt(tab, i) == first) {
                        validated = true;
                        Node<K, V> e = first, pred = null;
                        while ((e = e.next) != null) {
                            K ek;
                            if (e.hash == hash &&
                                    ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                                V ev = e.value;
                                if (cv == null || cv == ev || (ev != null && cv.equals(ev))) {
                                    oldValue = ev;
                                    if (value != null) {
                                        //替换值
                                        e.value = value;
                                    } else if (pred != null) {
                                        //匹配到的节点不是第一个节点
                                        pred.next = e.next;
                                    } else {
                                        //匹配到的节点是第一个节点
                                        setTabAt(tab, i, e.next);
                                    }
                                    break;
                                }
                                pred = e;
                            }
                        }
                    }

                }
                // validated==true 表示已经run过核心的查找逻辑了，所以在内部就可以break
                if (validated) {
                    if (oldValue != null) {
                        if (value == null) {
                            //说明是remove操作
                            addCount(-1L, -1);
                        }
                        return oldValue;
                    }
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es = entrySet;
        return entrySet == null ? (entrySet = new EntrySet()) : es;
    }

    @Override
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        tryPresize(m.size());
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public V get(Object key) {
        Node<K, V>[] tab;
        Node<K, V> e, p;
        int n, eh;
        K ek;
        int hash = spread(key.hashCode());
        if ((tab = table) != null
                && (n = tab.length) > 0
                && (e = tabAt(tab, (n - 1) & hash)) != null) {

            if ((eh = e.hash) == hash) {
                //查找到的就是当前节点
                if (((ek = e.key) == key) || (ek != null && key.equals(ek))) {
                    return e.value;
                }
            } else if (eh < 0) {
                // 说明该节点已经是扩容迁移后的ForwardingNode节点了
                //这里就是扩容时get也不需要加锁的关键
                return (p = e.find(hash, key)) != null ? p.value : null;
            }

            while ((e = e.next) != null) {
                if (e.hash == hash &&
                        ((ek = e.key) == key || (ek != null && key.equals(ek)))) {

                    return e.value;
                }
            }
        }
        return null;
    }

    /**
     * MyConcurrentHashMap中value也不能null，如果为null，这一步返回的结果就是不正确的
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public V remove(Object key) {
        return replaceNode(key, null, null);
    }

    /**
     * 添加size个元素，调整table大小以试以适应一次性添加size个元素
     *
     * @param size
     */
    private void tryPresize(int size) {

    }

    /**
     * 添加一个值
     *
     * @param key
     * @param value
     * @param onlyIfAbsent 如果存在就不覆盖该值
     * @return
     */
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (null == key) {
            throw new NullPointerException();
        }
        int hash = spread(key.hashCode());
        Node<K, V>[] tab;
        for (tab = table; ; ) {
            Node<K, V> first;//头结点
            int i, n;
            if (null == tab || (n = tab.length) == 0) {
                //如果tabl为null，说明表还没有初始化，需要进行初始化工作
                tab = initTable();
            } else if ((first = tabAt(tab, i = (n - 1) & hash)) == null) {
                // 如果头结点为空，那么就直接设置值，不需要加锁
                if (casTabAt(tab, i, null, newNode(hash, key, value, null))) {
                    break;
                }
            } else if (sizeCtl.get() == MOVED) {
                //说明正在扩容， 帮助扩容
                tab = helpTransfer(tab, first);
            } else {
                V oldValue = null;
                synchronized (first) {
                    // 锁定头结点将值设置到链表中去,这里放到链表末尾
                    Node<K, V> currentNode = first;
                    while (currentNode.next != null) {
                        K currKey = currentNode.key;
                        if (hash == currentNode.hash && (key == currKey || (currKey != null && currKey.equals(key)))) {
                            //说明设置的值已经存在了
                            oldValue = currentNode.value;
                            if (!onlyIfAbsent) {
                                currentNode.value = value;
                            }
                            break;
                        }
                        if ((currentNode = currentNode.next) == null) {
                            currentNode.next = newNode(hash, key, value, null);
                            break;
                        }
                    }
                }
                // 这里oldValue不为空说明该key值已经存在过，只是一个修改操作，所以就直接返回了
                if (oldValue != null) {
                    return oldValue;
                }
                break;
            }
        }
        addCount(1L, 1);
        return null;
    }

    /**
     * 成功添加元素之后进行元素计数的累计操作，并检查是否需要进行扩容操作，如果需要就进行扩容
     *
     * @param x     添加元素个数
     * @param check 检查是否进行扩容操作， if<0不检查，if>=0检查
     */
    private final void addCount(long x, int check) {
        //这里添加x是一个原子操作，不会存在多个线程同时添加时，某个线程无法感知其他线程的累加操作
        long size = baseCount.addAndGet(x);
        if (check >= 0) {
            int n, sc;
            Node<K, V>[] tab, nt;
            while ((n = table.length) < DEFAULT_MAX_CAPACITY &&  //没有达到数组的最大长度才允许扩容
                    (tab = table) != null && //保证初始化之后才进行扩容操作
                    size >= (sc = sizeCtl.get())) { //达到扩容阈值
                if (sc < 0) {
                    //说明已经有线程在进行扩容了
                    if ((nt = newTable) == null || //说明扩容主线程要么还没开始，要么已经结束了，没开始的概率非常小
                            transferIndex.get() <= 0 || //已经扩容完毕了
                            ((-sc - 1) >= MAX_RESIZERS)) {//帮助扩容的线程数已经达到最大值了
                        break;
                    }
                    if (sizeCtl.compareAndSet(sc, sc - 1)) {
                        //帮助扩容
                        transfer(tab, nt);
                    }
                } else if (sizeCtl.compareAndSet(sc, -1)) {
                    //说明需要当前线程进行扩容操作
                    transfer(tab, null);
                }
                size = baseCount.get();
            }
        }
    }

    /**
     * 扩容操作
     *
     * @param tab
     * @param nextTab 为null时表示需要当前线程创建newTable
     */
    private void transfer(Node<K, V>[] tab, Node<K, V>[] nextTab) {
        int n = tab.length, stride;
        //计算需要迁移多少个hash桶，MIN_TRANSFER_STRIDE该值作为下限，避免扩容线程过多
        if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE) {
            stride = MIN_TRANSFER_STRIDE; // 保证最少在16个槽以上
        }

        if (nextTab == null) {
            //说明是扩容主线程，需要创建新的数组
            try {
                Node<K, V>[] nt = (Node<K, V>[]) new Node<?, ?>[n << 1];
                nextTab = nt;
            } catch (Throwable e) {
                //防止内存溢出
                sizeCtl.set(Integer.MAX_VALUE);
                return;
            }
            newTable = nextTab;
            transferIndex.set(n);
        }

        int nextn = nextTab.length;
        ForwardingNode<K, V> fwd = new ForwardingNode<>(nextTab);
        boolean advance = true; //CAS循环获取扩容位置的标志
        boolean finishing = false;
        //1 逆序迁移已经获取到的hash桶集合，如果迁移完毕，则更新transferIndex，获取下一批待迁移的hash桶
        //2 如果transferIndex=0，表示所以hash桶均被分配，将i置为-1，准备退出transfer方法
        for (int i = 0, bound = 0; ; ) {
            Node<K, V> first;int fh;

            //CAS方式获取本次扩容的坐标位置
            while (advance) {
                int nextIndex, nextBound;
                if (--i >= bound || finishing) {
                    advance = false;
                } else if ((nextIndex = transferIndex.get()) <= 0) {
                    //transferIndex<=0表示已经没有需要迁移的hash桶，将i置为-1，线程准备退出
                    i = -1;
                    advance = false;
                } else if (transferIndex.compareAndSet(nextIndex,
                        nextBound = (nextIndex > stride ? nextIndex - stride : 0))) {
                    bound = nextBound;
                    i = nextIndex - 1;
                    advance = false;
                }
            }

            if (i < 0 || i >= n || i + n >= nextn) {
                int sc;
                if (finishing) {
                    //最后一个迁移的线程，进行一些收尾工作
                    newTable = null;
                    table = nextTab;
                    sizeCtl.set((n << 1) - (n >>> 1));
                    return;
                }
                if (sizeCtl.compareAndSet(sc = sizeCtl.get(), sc - 1)) {
                    if((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT) {
                        return;
                    }
                    finishing = advance = true;
                    i = n;
                }
            } else if((first = tabAt(tab, i)) == null) {
                advance = casTabAt(tab, i, null, fwd);
            } else if((fh = first.hash) == MOVED) {
                advance = true;
            } else {
                synchronized (first){
                    //开始对first槽点的链表数据进行迁移
                    if(tabAt(tab, i) == first) {
                        //新数组长度
                        int l = n << 1;
                        for(Node<K,V> current = first; current.next != null; current=current.next) {
                            // 找到当前节点迁移到新节点上的槽点位置
                            int location = (l - 1) & current.hash;
                            //这里不能直接移动当前节点，因为这些节点在迁移完成之前还需要对外提供服务
                            Node<K,V> newNode = new Node<>(current.hash, current.key, current.value, tabAt(newTable, location));
                            setTabAt(nextTab, location, newNode);
                        }
                        setTabAt(tab, i, fwd);
                        advance = true;
                    }
                }
            }
        }
    }

    private int resizeStamp(int n) {
        return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));
    }

    private Node<K, V>[] helpTransfer(Node<K, V>[] tab, Node<K, V> first) {
        return null;
    }

    /**
     * 初始化数组
     *
     * @return
     */
    private Node<K, V>[] initTable() {
        Node<K, V>[] tab;
        int sc;
        while ((tab = table) == null || tab.length == 0) {
            if ((sc = sizeCtl.get()) < 0) {
                //说明有其他线程已经在进行初始化了
                Thread.yield();
            } else if (sizeCtl.compareAndSet(sc, -1)) {
                //cas方式将sizeCtl值设为-1，设置成功就可以认为当前线程获取到锁了，能够进行初始化工作了
                try {
                    if ((tab = table) == null || tab.length == 0) {
                        int n = sc > 0 ? sc : DEFAULT_MIN_CAPACITY;
                        Node<K, V>[] nt = (Node<K, V>[]) new Node<?, ?>[n];
                        table = tab = nt;
                        // 设置扩容阈值
                        sc = n - (n >> 2);
                    }
                } finally {
                    sizeCtl.set(sc);
                }
                break;
            }
        }
        return tab;
    }

    /**
     * 计算hash
     *
     * @param h
     * @return
     */
    private int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;
    }

    static class Node<K, V> implements Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        /**
         * Virtualized support for map.get(); overridden in subclasses.
         */
        Node<K, V> find(int h, Object k) {
            Node<K, V> e = this;
            if (k != null) {
                do {
                    K ek;
                    if (e.hash == h &&
                            ((ek = e.key) == k || (ek != null && k.equals(ek)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
            return null;
        }
    }

    /**
     * 转运节点，用于旧数组的槽点扩容完成之后占用槽点的节点类
     *
     * @param <K>
     * @param <V>
     */
    static final class ForwardingNode<K, V> extends Node<K, V> {
        final Node<K, V>[] nextTable;

        public ForwardingNode(Node<K, V>[] nextTable) {
            super(MOVED, null, null, null);
            this.nextTable = nextTable;
        }

        /**
         * 从新数组中查找指定key值的节点
         *
         * @param n   新数组中的槽点位置
         * @param key 查找的key
         * @return
         */
        @Override
        Node<K, V> find(int n, Object key) {
            return null;
        }
    }

    final class EntrySet extends AbstractSet<Entry<K, V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public Spliterator<Entry<K, V>> spliterator() {
            return null;
        }

        @Override
        public int size() {
            long size = baseCount.get();
            if (size > Integer.MAX_VALUE) {
                size = Integer.MAX_VALUE;
            }
            return (int) size;
        }

    }

    final class EntryIterator extends HashIterator implements Iterator<Entry<K, V>> {
        @Override
        public final Entry<K, V> next() {
            return nextNode();
        }
    }

    abstract class HashIterator {
        Node<K, V> next;        // next entry to return
        Node<K, V> current;     // current entry
        int index;             // current slot

        HashIterator() {
            Node<K, V>[] tab = table;
            current = next = null;
            index = 0;
            if (tab != null && baseCount.get() > 0) {
                while (index < tab.length && next == null) {
                    next = tab[index++];
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K, V> nextNode() {
            Node<K, V>[] t;
            Node<K, V> e = next;

            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null) {
                while (index < t.length && next == null) {
                    next = t[index++];
                }
            }
            return e;
        }

        public final void remove() {
            Node<K, V> p = current;
            if (p == null) {
                throw new IllegalStateException();
            }

            current = null;
            K key = p.key;
           replaceNode(key, null, null);
        }
    }

    final class KeyIterator extends HashIterator implements Iterator<K> {
        @Override
        public final K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator implements Iterator<V> {
        @Override
        public final V next() {
            return nextNode().value;
        }
    }

}

