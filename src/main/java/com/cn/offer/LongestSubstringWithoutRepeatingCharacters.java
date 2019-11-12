package com.cn.offer;

import java.util.HashSet;
import java.util.Set;

/**
 * @autor: lvbiao
 * @version: 1.0
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * 示例 1:
 * <p>
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * <p>
 * 链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
 */
public class LongestSubstringWithoutRepeatingCharacters {

    /**
     * 思路： 该问题可以利用滑动窗口的思想来解决。
     * 两个指针，一个从头开始找，一个不动，直到另一个找到相同的
     *
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n) {
            if(!set.contains(s.charAt(j))){
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            }else {
                set.remove(s.charAt(i++));
            }
        }
        return ans;





/*
        Integer[] indexArray = new Integer[26];
        int length = 0;
        int partLength = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = (c - 'a');
            Integer sIndex = indexArray[index];
            if (sIndex == null) {
                partLength++;
                length = Math.max(length, partLength);
            } else {
                int currentLength = i - sIndex;
                length = Math.max(length, currentLength);
                partLength = 0;
            }
            indexArray[index] = i;
        }
        return length;*/
    }
}
