package com.cn.datastructure.concurrent;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;

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
    /**
     * 节点数组,长度总是2的n次方
     */
    transient Node<K, V>[] table;
    /**
     * 扩容时的新数组，只有在扩容的时候才不为空
     */
    transient Node<K, V>[] newTable;
    transient Set<Entry<K, V>> entrySet;
    /**
     * 下一次需要扩容的临界值， table.length * DEFAULT_LOAD_FACTOR
     */
    int threshold;
    /**
     * 当前ConcurrentHashMap的基本大小，
     * 这里纪录的只是一个基本值，一个线程put完成之后会对该值进行CAS操作，如果CAS失败的话，该值纪录数会小于实际大小
     */
    private transient volatile long baseCount = 0;
    /**
     * 纪录当前ConcurrentHashMap的状态
     *  sizeCtl<-1 表示当前正在有 -sizeCtl-1个线程正在进行扩容操作
     *  sizeCtl==-1 表示当前正在进行初始化操作
     *  sizeCtl=0  表示没有指定初始容量
     *  sizeCtl>0  在初始化之前表示初始容量，在初始化之后表示需要扩容的阈值，
     *  除初始阈值指定负载因子外，其他自动计算扩容时计算阈值的方式：table.size*2 - table.size/2
     */
    private transient volatile int sizeCtl;
    /*
     * Encodings for Node hash fields. See above for explanation.
     */
    static final int MOVED     = -1; // hash for forwarding nodes
//    static final int TREEBIN   = -2; // hash for roots of trees
//    static final int RESERVED  = -3; // hash for transient reservations
//    static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash


//    final float loadFactor = 0.75f;
    public MyConcurrentHashMap() {
        this.sizeCtl = DEFAULT_MIN_CAPACITY;
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
     *
     * @param initialCapacity
     * @param loadFactor
     */
    public MyConcurrentHashMap(int initialCapacity, final float loadFactor) {
        if (initialCapacity < 0 || loadFactor < 0 || loadFactor > 1) {
            throw new IllegalArgumentException();
        }
        //这里+1是向上取整
        long size = (long)((long)initialCapacity/loadFactor + 1.0f);
        int cap = tableSizeFor(size);
        this.sizeCtl = cap;
    }

    public MyConcurrentHashMap(Map<? extends K, ? extends V> m) {
        this.sizeCtl = DEFAULT_MIN_CAPACITY;
        putAll(m);
    }

    /**
     * hash值计算
     *
     * @param key
     * @return
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
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

    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node(hash, key, value, next);
    }

    /**
     * 获取节点
     * key的数组位置为： hash(key) & (table.length - 1)
     * 每次扩容后都需要重新设置key的位置
     *
     * @param hash
     * @param key
     * @return
     */
    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab = table;
        if (null != tab && tab.length > 0) {
            Node<K, V> first = tab[hash & (tab.length - 1)];
            if (first != null) {
                if (hash == first.hash && (first.key == key || (key != null && key.equals(first.key)))) {
                    return first;
                } else if (null != first.next) {
                    Node<K, V> node = first;
                    while (null != node.next) {
                        node = node.next;
                        if (hash == node.hash && (node.key == key || (key != null && key.equals(node.key)))) {
                            return node;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 删除节点
     *
     * @param hash
     * @param key
     * @param value
     * @param matchValue true：值匹配的时候才移除
     * @return
     */
    final Node<K, V> removeNode(int hash, Object key, Object value, boolean matchValue) {
        Node<K, V>[] tab = table;
        Node<K, V> lastNode = null;//上一个节点
        Node<K, V> currentNode = null;//当前节点
        if (null != tab && tab.length > 0) {
            int firstLocal = hash & (tab.length - 1);
            Node<K, V> first = tab[firstLocal];
            if (first != null) {
                if (hash == first.hash && (first.key == key || (key != null && key.equals(first.key)))) {
                    //第一个节点就是
                    currentNode = first;
                } else if (null != first.next) {
                    Node<K, V> node = first;
                    while (null != node.next) {
                        lastNode = node;
                        node = node.next;
                        if (hash == node.hash && (node.key == key || (key != null && key.equals(node.key)))) {
                            currentNode = node;
                            break;
                        }
                    }
                }
                if (null != currentNode) {
                    if (!matchValue || (matchValue && value != null && value.equals(currentNode.value))) {
                        //说明是第一个节点
                        if (null == lastNode) {
                            tab[firstLocal] = currentNode.next;
                        } else {
                            lastNode.next = currentNode.next;
                        }
                        baseCount--;
                        return currentNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 扩容
     *
     * @return
     */
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            //如果容量已经达到最大了，就不能再扩容了
            if (oldCap >= DEFAULT_MAX_CAPACITY) {
                threshold = DEFAULT_MAX_CAPACITY;
                return oldTab;
            } else if ((newCap = oldCap << 1) < DEFAULT_MAX_CAPACITY && oldCap > DEFAULT_MIN_CAPACITY) {
                //扩容2倍
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            //跑到这里表明是初始化时指明了容器初始化大小的调用的，这里的容量有可能小于16
            newCap = oldThr;
        } else {
            newCap = DEFAULT_MIN_CAPACITY;
            //HashMap中这里使用的默认的负载因子，暂时未发现为什么这么指定，
            // 如果使用默认负载因子，那么新建Map<>时指定的负载因子大小就不会生效，所以这里暂时先使用指定的大小
            newThr = (int) (newCap * loadFactor);
        }

        //
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < DEFAULT_MAX_CAPACITY && ft < (float) DEFAULT_MAX_CAPACITY ?
                    (int) ft : DEFAULT_MAX_CAPACITY);
        }

        //到这里扩容的大小就已经计算出来了
        Node<K, V>[] newTab = new Node[newCap];
        threshold = newThr;
        table = newTab;
        if (null != oldTab) {
            //将老的数据重新整理放到新的Node节点中
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> e = oldTab[i];
                if (null != e) {
                    oldTab[i] = null;
                    newTab[e.hash & (newCap - 1)] = e;
                }
            }
        }
        return newTab;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es = entrySet;
        return entrySet == null ? (entrySet = new EntrySet()) : es;
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        tryPresize(m.size());
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public V get(Object key) {
        Node<K, V> e = getNode(hash(key), key);
        return e == null ? null : e.value;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> e = removeNode(hash(key), key, null, false);
        return e == null ? null : e.value;
    }

    /**
     * 添加size个元素，调整table大小以试以适应一次性添加size个元素
     * @param size
     */
    private void tryPresize(int size) {

    }

    /**
     * 添加一个值
     *
     * @param hash
     * @param key
     * @param value
     * @param onlyIfAbsent 如果存在就不覆盖该值
     * @return
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent) {
        Node<K, V>[] tab = table;

        if (null == tab || tab.length == 0) {
            tab = resize();
        }

        int length = tab.length;
        Node<K, V> first = tab[(length - 1) & hash];
        if (first == null) {
            //创建节点
            tab[(length - 1) & hash] = newNode(hash, key, value, null);
        } else {
            Node<K, V> e = null;
            if (hash == first.hash && (first.key == key || (key != null && key.equals(first.key)))) {
                //第一个节点的key就是需要添加的key
                e = first;
            } else if (null != first.next) {
                Node<K, V> node = first;
                while (null != node.next) {
                    node = node.next;
                    if (hash == node.hash && (node.key == key || (key != null && key.equals(node.key)))) {
                        e = node;
                        break;
                    }
                }
                if (null == e) {
                    node.next = newNode(hash, key, value, null);
                }
            }

            //这里如果为true，就说明之前这个key是存在的
            if (null != e) {
                V oldValue = e.value;
                if (!onlyIfAbsent || null == e.value) {
                    e.value = value;
                }
                return oldValue;
            }
        }

        if (++baseCount > threshold) {
            resize();
        }
        return null;
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
    }

    /**
     * 转运节点，用于旧数组的槽点扩容完成之后占用槽点的节点类
     * @param <K>
     * @param <V>
     */
    static final class ForwardingNode<K,V> extends Node<K,V> {
        final Node<K,V>[] nextTable;
        public ForwardingNode(Node<K,V>[] nextTable) {
            super(MOVED, null, null, null);
            this.nextTable = nextTable;
        }

        /**
         * 从新数组中查找指定key值的节点
         * @param n 新数组中的槽点位置
         * @param key 查找的key
         * @return
         */
        Node<K, V> find(int n, Object key){
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
            return (int) baseCount;
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
            if (tab != null && baseCount > 0) {
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
            removeNode(hash(key), key, null, false);
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

