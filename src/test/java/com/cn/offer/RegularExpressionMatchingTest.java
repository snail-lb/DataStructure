package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegularExpressionMatchingTest {

    @Test
    public void isMatch() {
        RegularExpressionMatching re = new RegularExpressionMatching();
        Assert.assertTrue(re.isMatch("", "a*"));
        Assert.assertTrue(re.isMatch("a", "a*"));
        Assert.assertTrue(re.isMatch("aa", "a*"));
        Assert.assertTrue(re.isMatch("aaa", "a*"));
        Assert.assertTrue(re.isMatch("aba", "a.a"));
    }
}