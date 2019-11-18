package com.cn.offer;

import org.junit.Assert;
import org.junit.Test;

public class BitOperationTest {

    @Test
    public void baseOperation() {
        BitOperation.baseOperation(123,456);
    }

    @Test
    public void operation() {
        BitOperation.operation();
    }

    @Test
    public void isEven() {
        Assert.assertEquals(BitOperation.isEven(5), false);
        Assert.assertEquals(BitOperation.isEven(6), true);
        Assert.assertEquals(BitOperation.isEven(0), true);
        Assert.assertEquals(BitOperation.isEven(-1), false);
        Assert.assertEquals(BitOperation.isEven(-2), true);
    }

    @Test
    public void swap() {
        BitOperation.swap(1, 2);
    }

    @Test
    public void negate() {
        Assert.assertEquals(BitOperation.negate(3), -3);
        Assert.assertEquals(BitOperation.negate(-3), 3);
        Assert.assertEquals(BitOperation.negate(0), 0);
    }

    @Test
    public void abs() {
        Assert.assertEquals(BitOperation.abs(3), 3);
        Assert.assertEquals(BitOperation.abs(-3), 3);
    }

    @Test
    public void isPowerOfTwo() {
        Assert.assertEquals(BitOperation.isPowerOfTwo(3), false);
        Assert.assertEquals(BitOperation.isPowerOfTwo(4), true);
        Assert.assertEquals(BitOperation.isPowerOfTwo(16), true);
        Assert.assertEquals(BitOperation.isPowerOfTwo(-16), false);
    }

    @Test
    public void isPowerOfFour() {
        Assert.assertEquals(BitOperation.isPowerOfFour(-1), false);
        Assert.assertEquals(BitOperation.isPowerOfFour(0), false);
        Assert.assertEquals(BitOperation.isPowerOfFour(1), true);
        Assert.assertEquals(BitOperation.isPowerOfFour(4), true);
        Assert.assertEquals(BitOperation.isPowerOfFour(15), false);
        Assert.assertEquals(BitOperation.isPowerOfFour(16), true);
        Assert.assertEquals(BitOperation.isPowerOfFour(256), true);
        Assert.assertEquals(BitOperation.isPowerOfFour(255), false);
    }


    @Test
    public void mod() {
        Assert.assertEquals(1, BitOperation.mod(5, 4));
        Assert.assertEquals(0, BitOperation.mod(4, 4));
        Assert.assertEquals(3, BitOperation.mod(3, 4));
        Assert.assertEquals(0, BitOperation.mod(0, 4));
    }
}