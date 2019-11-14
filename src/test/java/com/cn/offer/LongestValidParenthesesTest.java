package com.cn.offer;

import org.junit.Test;

import junit.framework.Assert;

import static org.junit.Assert.*;

public class LongestValidParenthesesTest {

    @Test
    public void longestValidParentheses() {
        LongestValidParentheses lvp = new LongestValidParentheses();
        Assert.assertEquals(4, lvp.longestValidParentheses("(()))"));
        Assert.assertEquals(0, lvp.longestValidParentheses("(((("));
        Assert.assertEquals(0, lvp.longestValidParentheses("))))"));
        Assert.assertEquals(0, lvp.longestValidParentheses(""));
        Assert.assertEquals(0, lvp.longestValidParentheses(null));
        Assert.assertEquals(4, lvp.longestValidParentheses("(())"));
    }
}