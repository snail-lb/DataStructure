package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolutionTest {

    @Test
    public void twoSum() {
        Solution solution = new Solution();
        int[] nums = {1,2,3,4,5,6,7};
        int targets = 12;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertArrayEquals(results, new int[]{4,6});
    }

    @Test
    public void twoSum1() {
        Solution solution = new Solution();
        int[] nums = {-1,-2,3,4,5,6,7};
        int targets = 5;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertArrayEquals(results, new int[]{0,5});
    }

    @Test
    public void twoSum2() {
        Solution solution = new Solution();
        int[] nums = {};
        int targets = 12;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertNull(results);
    }

    @Test
    public void twoSum3() {
        Solution solution = new Solution();
        int[] nums = {1,2,3,4,5,6,7};
        int targets = 14;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertNull(results);
    }

    @Test
    public void twoSum4() {
        Solution solution = new Solution();
        int[] nums = {3, 3};
        int targets = 6;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertArrayEquals(results, new int[]{0,1});
    }
}