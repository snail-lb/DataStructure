package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZuiChangHuiWenZiChuanByLeetcodeTest {

    @Test
    public void test1() {
        ZuiChangHuiWenZiChuanByLeetcode zc = new ZuiChangHuiWenZiChuanByLeetcode();
        Assert.assertEquals("babab", zc.longestPalindrome("babab"));
        Assert.assertEquals("baab", zc.longestPalindrome("baab"));
        Assert.assertEquals("a", zc.longestPalindrome("a"));
        Assert.assertEquals("d", zc.longestPalindrome("abcd"));
        Assert.assertEquals("aaaaa", zc.longestPalindrome("aaaaa"));
        Assert.assertEquals("aaaa", zc.longestPalindrome("aaaa"));
        Assert.assertEquals("", zc.longestPalindrome(""));
        Assert.assertEquals("", zc.longestPalindrome(null));
    }

}