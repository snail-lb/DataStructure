package com.cn.offer;

/**
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
 * <p>
 * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
 * <p>
 * L   C   I   R
 * E T O E S I I G
 * E   D   H   N
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
 * <p>
 * 请你实现这个将字符串进行指定行数变换的函数：
 * <p>
 * https://leetcode-cn.com/problems/zigzag-conversion/
 *
 * @author: lvbiao
 * @date: 2019-11-11 15:51
 */
public class ZigzagConversion {

    /**
     * 思路1：将该字符串转为二维数组存储，最后遍历二维数组，这种内存消耗会比较大
     * 思路2：整个结构可以分为下列这种部分，每部分有(2*n-2)个元素(n>2),假设为m
     * 然后将源字符串分为k段，每段m个，遍历n次，第一次遍历第1个元素，第二次遍历第2个和第m个，第三次遍历第3个和m-1个...
     * 将遍历结果顺序存储即可得到最终结果
     * L     C     I     R
     * E T   O E   S I   I G
     * E     D     H     N
     *
     * @param s
     * @param numRows
     * @return
     */
    public String convert(String s, int numRows) {
        if (s == null || s.length() == 0) {
            return "";
        }
        if (numRows == 1) {
            return s;
        }

        char[] chars = s.toCharArray();
        int length = chars.length;
        char[] result = new char[length];
        /*每部分的数量*/
        int m = 2 * numRows - 2;
        /*记录result设置值的位置*/
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            /*每行的时候遍历每部分*/
            for (int j = 0; j + i < length; j += m) {
                result[index++] = chars[j + i];
                if (i != 0 && i != numRows - 1 && j + m - i < length) {
                    result[index++] = chars[j + m - i];
                }
            }
        }
        return String.valueOf(result);
    }
}
