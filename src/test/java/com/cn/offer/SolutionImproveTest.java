package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

public class SolutionImproveTest {

    @Test
    public void twoSum() {
        SolutionImprove solution = new SolutionImprove();
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        int targets = 12;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertArrayEquals(results, new int[]{4, 6});
    }

    @Test
    public void twoSum1() {
        SolutionImprove solution = new SolutionImprove();
        int[] nums = {-1, -2, 3, 4, 5, 6, 7};
        int targets = 5;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertArrayEquals(results, new int[]{0, 5});
    }

    @Test
    public void twoSum2() {
        SolutionImprove solution = new SolutionImprove();
        int[] nums = {};
        int targets = 12;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertNull(results);
    }

    @Test
    public void twoSum3() {
        SolutionImprove solution = new SolutionImprove();
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        int targets = 14;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertNull(results);
    }

    @Test
    public void twoSum4() {
        SolutionImprove solution = new SolutionImprove();
        int[] nums = {3, 3};
        int targets = 6;
        int[] results = solution.twoSum(nums, targets);
        Assert.assertArrayEquals(results, new int[]{0, 1});
    }
}