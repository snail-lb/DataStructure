package com.cn.datastructure;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;

/**
 * Created by lvbiao on 2018/7/2.
 */
public class MyHashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {

    private static final long serialVersionUID = 362498820763181982L;

    //默认的初始化容量
    transient final static Integer DEFAULT_MIN_CAPACITY = 16;
    //map最大容量
    transient final static Integer DEFAULT_MAX_CAPACITY = 1 << 30;

    //当前map的大小
    protected transient int size = 0;

    //节点数组
    transient Node<K,V>[] table;
    //key
    transient Set<Entry<K,V>> entrySet;
    /*默认的负载因子，就是如果map中元素个数超过了threshold，就进行扩容
    这个值如果比较大，那么给table扩容的可能性就会降低，但是每条node链上的元素就会较多，占用的内存就比较小，但是查询时间就会变长，
    这个值如果比较小，那扩容的可能性就会增大，占用内存会更大，node链上的元素就会少一些，查询时间会更短*/
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //实际使用的负载因子
    final float loadFactor;
    //下一次需要扩容的临界值， table.length * DEFAULT_LOAD_FACTOR
    int threshold;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, final float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > DEFAULT_MAX_CAPACITY)
            initialCapacity = DEFAULT_MAX_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K, V> next){
        return new Node(hash,key,value,next);
    }

    /**
     * 获取节点
     * key的数组位置为： hash(key) & (table.length - 1)
     * 每次扩容后都需要重新设置key的位置
     * @param hash
     * @param key
     * @return
     */
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab = table;
        if(null != tab && tab.length > 0){
            Node<K,V> first = tab[hash & (tab.length-1)];
            if(first != null){
                if(hash == first.hash && (first.key==key || (key!=null && key.equals(first.key)))){
                    return first;
                }else if(null != first.next){
                    Node<K,V> node = first;
                    while (null != node.next){
                        node = node.next;
                        if(hash == node.hash && (node.key==key || (key!=null && key.equals(node.key)))){
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
     * @param hash
     * @param key
     * @param value
     * @param matchValue true：值匹配的时候才移除
     * @return
     */
    final Node<K,V> removeNode(int hash, Object key, Object value, boolean matchValue) {
        Node<K,V>[] tab = table;
        Node<K,V> lastNode=null;//上一个节点
        Node<K,V> currentNode=null;//当前节点
        if(null != tab && tab.length > 0){
            int firstLocal = hash & (tab.length-1);
            Node<K,V> first = tab[firstLocal];
            if(first != null){
                if(hash == first.hash && (first.key==key || (key!=null && key.equals(first.key)))){
                    //第一个节点就是
                    currentNode = first;
                }else if(null != first.next){
                    Node<K,V> node = first;
                    while (null != node.next){
                        lastNode = node;
                        node = node.next;
                        if(hash == node.hash && (node.key==key || (key!=null && key.equals(node.key)))){
                            currentNode = node;
                            break;
                        }
                    }
                }
                if(null != currentNode){
                    if(!matchValue || (matchValue && value!=null && value.equals(currentNode.value))) {
                        //说明是第一个节点
                        if (null == lastNode) {
                            tab[firstLocal] = currentNode.next;
                        } else {
                            lastNode.next = currentNode.next;
                        }
                        size--;
                        return currentNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * hash值计算
     * @param key
     * @return
     */
    static final int hash(Object key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 计算cap个节点时，table的大小
     * 满足 2^n >= cap 这个条件的n的最小值
     * @param cap
     * @return
     */
    static final int tableSizeFor(int cap) {
        if(cap < 0){
            throw new IllegalArgumentException("Illegal initial cap: " + cap);
        }
        int n = 1,size = 1;
        while (size < cap){
            n++;
            size = 1 << n;
        }
        return size>DEFAULT_MAX_CAPACITY?DEFAULT_MAX_CAPACITY:size;
    }

    /**
     * 扩容
     * @return
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if(oldCap > 0){
            //如果容量已经达到最大了，就不能再扩容了
            if(oldCap >= DEFAULT_MAX_CAPACITY){
                threshold = DEFAULT_MAX_CAPACITY;
                return oldTab;
            }else if((newCap=oldCap<<1) < DEFAULT_MAX_CAPACITY && oldCap > DEFAULT_MIN_CAPACITY){
                //扩容2倍
                newThr = oldThr << 1;
            }
        }else if(oldThr > 0){
            //跑到这里表明是初始化时指明了容器初始化大小的调用的，这里的容量有可能小于16
            newCap = oldThr;
        }else{
            newCap = DEFAULT_MIN_CAPACITY;
            //HashMap中这里使用的默认的负载因子，暂时未发现为什么这么指定，
            // 如果使用默认负载因子，那么新建Map<>时指定的负载因子大小就不会生效，所以这里暂时先使用指定的大小
            newThr = (int) (newCap * loadFactor);
        }

        //
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < DEFAULT_MAX_CAPACITY && ft < (float)DEFAULT_MAX_CAPACITY ?
                    (int)ft : DEFAULT_MAX_CAPACITY);
        }

        //到这里扩容的大小就已经计算出来了
        Node<K,V>[] newTab = new Node[newCap];
        threshold = newThr;
        table=newTab;
        if(null != oldTab){
            //将老的数据重新整理放到新的Node节点中
            for (int i = 0; i < oldCap; i++) {
                Node<K,V> e = oldTab[i];
                if(null != e){
                    oldTab[i] = null;
                    newTab[e.hash & (newCap-1)] = e;
                }
            }
        }
        return newTab;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K,V>> es = entrySet;
        return entrySet == null ? (entrySet = new EntrySet()) : es;
    }

    @Override
    public V put(K key, V value){
        return putVal(hash(key), key, value, false);
    }

    @Override
    public V get(Object key) {
        Node<K,V> e = getNode(hash(key), key);
        return e == null ? null : e.value;
    }

    public V remove(Object key) {
        Node<K,V> e = removeNode(hash(key), key, null, false);
        return e == null ? null : e.value;
    }

    /**
     * 添加一个值
     * @param hash
     * @param key
     * @param value
     * @param onlyIfAbsent 如果存在就不覆盖该值
     * @return
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent) {
        Node<K,V>[] tab = table;

        if(null == tab || tab.length == 0){
            tab = resize();
        }

        int length = tab.length;
        Node<K,V> first = tab[(length - 1) & hash];
        if(first == null){
            //创建节点
            tab[(length - 1) & hash] = newNode(hash, key, value, null);
        }else {
            Node<K,V> e = null;
            if(hash == first.hash && (first.key==key || (key!=null && key.equals(first.key)))){
                //第一个节点的key就是需要添加的key
                e = first;
            }else if(null != first.next){
                Node<K,V> node = first;
                while (null != node.next){
                    node = node.next;
                    if(hash == node.hash && (node.key==key || (key!=null && key.equals(node.key)))){
                        e = node;
                        break;
                    }
                }
                if(null == e){
                    node.next = newNode(hash, key, value, null);
                }
            }

            //这里如果为true，就说明之前这个key是存在的
            if(null != e){
                V oldValue = e.value;
                if(!onlyIfAbsent || null == e.value){
                    e.value = value;
                }
                return oldValue;
            }
        }

        if (++size > threshold) {
            resize();
        }
        return null;
    }

    static class Node<K,V> implements Entry<K,V>{
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key +"=" + value;
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

    final class EntrySet extends AbstractSet<Entry<K,V>>{

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
            return size;
        }

    }

    final class EntryIterator extends HashIterator implements Iterator<Entry<K,V>> {
        public final Entry<K,V> next() {
            return nextNode();
        }
    }

    abstract class HashIterator {
        Node<K,V> next;        // next entry to return
        Node<K,V> current;     // current entry
        int index;             // current slot

        HashIterator() {
            Node<K,V>[] tab = table;
            current = next = null;
            index = 0;
            if (tab != null && size > 0) {
                while (index < tab.length && next == null){
                    next = tab[index++];
                }
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;

            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null) {
                while (index < t.length && next == null){
                    next = t[index++];
                }
            }
            return e;
        }

        public final void remove() {
            Node<K,V> p = current;
            if (p == null) {
                throw new IllegalStateException();
            }

            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false);
        }
    }

    final class KeyIterator extends HashIterator
            implements Iterator<K> {
        public final K next() { return nextNode().key; }
    }

    final class ValueIterator extends HashIterator
            implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }

}
