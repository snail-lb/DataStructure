package com.cn.offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @autor: lvbiao
 * @version: 1.0
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * <p>
 * https://leetcode-cn.com/problems/two-sum/
 */
public class Solution {
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if(map.containsKey(nums[i])) {
                map.get(nums[i]).add(i);
            }else {
                List<Integer> lists = new ArrayList<>();
                lists.add(i);
                map.put(nums[i], lists);
            }
        }

        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            int b = target - a;
            if (map.containsKey(b)) {
                List<Integer> lists = map.get(b);
                if (a == b) {
                    if (lists.size() > 1)
                    return new int[]{lists.get(0), lists.get(1)};
                } else {
                    List<Integer> lista = map.get(a);
                    if (!lista.equals(lists)) {
                        return new int[]{lista.get(0), lists.get(0)};
                    }
                }
            }
        }
        return null;
    }
}
