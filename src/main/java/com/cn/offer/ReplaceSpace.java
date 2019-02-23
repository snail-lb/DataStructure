package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-23 18:42
 * 剑指offer第二版 51页 替换空格
 * 题目：请实现一个函数，将一个字符串中的每个空格替换成“%20”。例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy
 */
public class ReplaceSpace {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("We Are Happy");
        String result = replaceSpace(sb);
        System.out.println(result);
    }

    public static String replaceSpace(StringBuffer str) {
        if (str == null) {
            return null;
        }
        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                newStr.append('%');
                newStr.append('2');
                newStr.append('0');
            } else {
                newStr.append(str.charAt(i));
            }
        }
        return newStr.toString();
    }
}
