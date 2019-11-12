package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

public class LongestSubstringWithoutRepeatingCharactersTest {

    @Test
    public void lengthOfLongestSubstring() {
        LongestSubstringWithoutRepeatingCharacters ls = new LongestSubstringWithoutRepeatingCharacters();
        Assert.assertEquals(ls.lengthOfLongestSubstring("abcabcd"), 4);
        Assert.assertEquals(ls.lengthOfLongestSubstring("a"), 1);
        Assert.assertEquals(ls.lengthOfLongestSubstring(""), 0);
        Assert.assertEquals(ls.lengthOfLongestSubstring("abcde"), 5);
        Assert.assertEquals(ls.lengthOfLongestSubstring("abcabcbb"), 3);
        Assert.assertEquals(ls.lengthOfLongestSubstring("abcddef"), 4);
    }
}