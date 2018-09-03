package com.cn.datastructure;

import java.util.Map;

/**
 * Created by lvbiao on 2018/7/3.
 */
public class MyHashMapTest {
    public static void main(String[] args){
        MyHashMap<String,String> map = new MyHashMap();
        map.put("name1", "biao1");
        map.put("name2", "biao2");
        map.put("name3", "biao3");
        for(Map.Entry<String,String> entry : map.entrySet()){
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        Map<String,String> map1 = new MyHashMap<>();
        map1.put("name4","baio4");
        map.putAll(map1);

        for(String key : map.keySet()){
            System.out.println(key);
        }

        map.remove("name3");

        for(String value : map.values()){
            System.out.println(value);
        }

        map.clear();

        System.out.println(map.toString());

    }

}