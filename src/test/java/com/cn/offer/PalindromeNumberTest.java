package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PalindromeNumberTest {

    @Test
    public void isPalindrome() {
        int a= 123321;
        PalindromeNumber pn = new PalindromeNumber();
        boolean result = pn.isPalindrome(a);
        Assert.assertTrue(result);
    }
}