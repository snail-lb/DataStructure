package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DuplicateNumberSearchTest {

    @Test
    public void duplicate() {
        int[] numbers = {1,2,3,4,2};
        int[] duplication = new int[1];
        DuplicateNumberSearch search = new DuplicateNumberSearch();
        search.duplicate(numbers, 5, duplication);
        Assert.assertEquals(2, duplication[0]);
    }
}