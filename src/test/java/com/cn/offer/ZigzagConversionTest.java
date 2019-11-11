package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZigzagConversionTest {

    @Test
    public void convert() {
        String s = "LEETCODEISHIRING";
        ZigzagConversion zc = new ZigzagConversion();
        String result = zc.convert(s, 3);
        System.out.println(result);
        Assert.assertEquals("LCIRETOESIIGEDHN", result);
    }

    @Test
    public void convert1() {
        String s = "LEETCODEISHIRING";
        ZigzagConversion zc = new ZigzagConversion();
        String result = zc.convert(s, 4 );
        System.out.println(result);
        Assert.assertEquals("LDREOEIIECIHNTSG", result);
    }

    @Test
    public void convert2() {
        String s = "LEETCODEISHIRI";
        ZigzagConversion zc = new ZigzagConversion();
        String result = zc.convert(s, 3 );
        System.out.println(result);
        Assert.assertEquals("LCIRETOESIIEDH", result);
    }
}