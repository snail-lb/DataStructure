package com.cn.offer;

import java.util.HashMap;
import java.util.Map;

/**
 * @autor: lvbiao
 * @version: 1.0
 * 两数之和改进
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * <p>
 * https://leetcode-cn.com/problems/two-sum/
 */
public class SolutionImprove {

    /**
     * 通过hash表查找查找速度, 为了解决hash冲突，只能用数组来存储索引位置
     * 改进： hash冲突时，直接判断冲突的两数是否能直接完成结果，能的话就直接返回
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int key = nums[i];
            if (map.containsKey(key)) {
                if (key * 2 == target) {
                    return new int[]{map.get(key), i};
                }
            } else {
                map.put(nums[i], i);
            }
        }

        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            int b = target - a;
            if (map.containsKey(b)) {
                int indexa = map.get(a);
                int indexb = map.get(b);
                if (indexa != indexb) {
                    return new int[]{indexa, indexb};
                }
            }
        }
        return null;
    }
}
