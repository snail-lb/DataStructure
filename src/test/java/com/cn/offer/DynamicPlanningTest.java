package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicPlanningTest {

    @Test
    public void maxSubSequence() {
        int[] array1 = null;
        int[] array2 = {};
        int[] array3 = {-1};
        int[] array4 = {-1,2};
        int[] array5 = {-1,2,8,-13,4,5,2};

        Assert.assertEquals(DynamicPlanning.maxSubSequence(array1), Integer.MIN_VALUE);
        Assert.assertEquals(DynamicPlanning.maxSubSequence(array2), Integer.MIN_VALUE);
        Assert.assertEquals(DynamicPlanning.maxSubSequence(array3), -1);
        Assert.assertEquals(DynamicPlanning.maxSubSequence(array4), 2);
        Assert.assertEquals(DynamicPlanning.maxSubSequence(array5), 11);

    }
}