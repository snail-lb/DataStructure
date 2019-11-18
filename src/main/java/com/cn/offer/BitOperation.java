package com.cn.offer;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript: 位运算
 * @date: 2019-10-13 10:50
 * <p>
 * &  位与  两个比特位都为 1 时，结果才为 1，否则为 0 （位与操作满足交换律和结合律，甚至分配律）
 * |  位或  两个比特位都为 0 时，结果才为 0，否则为 1 （位或操作满足交换律和结合律，甚至分配律）
 * ~  位非  即按位取反，1 变 0，0 变 1
 * ^  异或  两个比特位相同时（都为 0 或都为 1）为 0，相异为 1（异或操作满足交换律和结合律，甚至分配律。任何整数和自己异或的结果为 0，任何整数与 0 异或值不变）
 * <<  左移  将所有的二进制位按值向←左移动若干位，左移操作与正负数无关，它只是傻傻地将所有位按值向左移动，高位舍弃，低位补 0
 * >>  右移  将所有的二进制位按值向右→移动若干位，低位直接舍弃，跟正负无关，而高位补 0 还是补 1 则跟被操作整数的正负相关，正数补 0 ，负数补 1
 * >>>  无符号右移  将所有的二进制位按值向右→移动若干位，低位直接舍弃，跟正负无关，高位补 0 ，也跟正负无关
 */
public class BitOperation {
    /**
     * 位运算基本操作
     * 以下操作全为true
     */
    public static void baseOperation(int a, int b) {
        System.out.println((a | 0) == a);
        System.out.println((a & -1) == a);
        System.out.println((a & 0) == 0);

        System.out.println((a ^ a) == 0);
        System.out.println((a & ~a) == 0);
        System.out.println((a & a) == a);
        System.out.println((a | a) == a);

        System.out.println((a | (a & b)) == a);
        System.out.println((a & (a | b)) == a);
    }

    public static void operation() {
        // 7. int 型最大值是什么?
        System.out.println((1 << 31) - 1);// 2147483647， 注意运算符优先级，括号不可省略
        System.out.println(~(1 << 31));// 2147483647

        // 8. int 型最小值是什么？
        System.out.println(1 << 31);
        System.out.println(1 << -1);

        // 9. long 型最大值是什么？
        System.out.println(((long) 1 << 127) - 1);

        // 10. 整数n乘以2是多少？
        int n = 123;
        System.out.println(n << 1);

        // 11. 整数n除以2是多少？(负奇数的运算不可用)
        System.out.println(n >> 1);

        // 12. 乘以2的n次方，例如计算10 * 8(8是2的3次方)
        System.out.println(10 << 3);

        // 13. 除以2的n次方，例如计算16 / 8(8是2的3次方)
        System.out.println(16 >> 3);

        // 14. 取两个数的最大值（某些机器上，效率比a>b ? a:b高）
        int a = 123, b = 456;
        System.out.println(b & ((a - b) >> 31) | a & (~(a - b) >> 31));

        // 15. 取两个数的最小值（某些机器上，效率比a>b ? b:a高）
        System.out.println(a & ((a - b) >> 31) | b & (~(a - b) >> 31));

        // 16. 判断符号是否相同(true 表示 x和y有相同的符号， false表示x，y有相反的符号。)
        System.out.println((a ^ b) > 0);

        // 17. 计算2的n次方 n > 0
        System.out.println(2 << (n - 1));

        // 18. 求两个整数的平均值
        System.out.println((a + b) >> 1);

        // 19. 从低位到高位,取n的第m位
        int m = 2;
        System.out.println((n >> (m - 1)) & 1);

        // 20. 从低位到高位.将n的第m位置为1，将1左移m-1位找到第m位，
        //得000...1...000，n 再和这个数做或运算
        System.out.println(n | (1 << (m - 1)));

        // 21. 从低位到高位,将n的第m位置为0，将1左移m-1位找到第m位，
        //取反后变成111...0...1111，n再和这个数做与运算
        System.out.println(n & ~(0 << (m - 1)));
    }

    /**
     * 判断奇偶数
     *
     * @param n
     */
    public static boolean isEven(int n) {
        return (n & 1) == 0;
    }

    /**
     * 省去中间变量交换两整数的值
     * 利用思想： 如果 c=a^b 那么 b==a^c, a==b^c
     *
     * @param a
     * @param b
     */
    public static void swap(int a, int b) {
        //假如 c = a^b
        a ^= b;//c=a^b
        b ^= a;//b=b^c
        a ^= b;//a=c^b
        System.out.println("a:" + a + " b:" + b);
    }

    /**
     * 计算相反数 整数变负，负数变正
     * 数在计算机的表示是用补码表示的，所以计算相反数是计算补码就行了
     *
     * @param n
     */
    public static int negate(int n) {
        return (~n) + 1;
    }

    /**
     * 计算绝对值
     * 对于负数可以通过上面变换符号的操作得到绝对值， 正数直接返回即可，因此我们要先判断符号位来得知当前数的正负。
     *
     * @param n
     * @return
     */
    public static int abs(int n) {
        int i = n >> 31;
        return i == 0 ? n : (~n) + 1;
    }

    /**
     * 判断一个数num是不是 2 的幂
     * 如果是2的幂，n一定是100... （也就是二进制位里只有一个是1，且是首位，其余全是0），n-1就是011.... 所以做与运算结果为 0
     *
     * @param n
     * @return
     */
    public static boolean isPowerOfTwo(int n) {
        if (n < 1) {
            return false;
        }
        return (n & (n - 1)) == 0;
    }

    /**
     * 判断一个数num是不是 4 的幂
     * 简单判断条件： 该数的二进制表达条件中只有一个1且这个1在奇数位
     *
     * @param n
     * @return
     */
    public static boolean isPowerOfFour(int n) {
        if (n < 1) {
            return false;
        }
        int numOfOne = 0;//1的数量
        int index = 1;//当前判断位置
        do {
            if ((n & 1) == 1) {
                if (numOfOne != 0) {
                    // 已经有一个1了，又发现一个1，不满足直接返回
                    return false;
                } else {
                    numOfOne++;
                    if ((n & 1) == 0) {
                        // 是在偶数位，不满足
                        return false;
                    }
                }
            }
            n >>= 1;
            // n等于0时就不需要循环判断了
            if (n == 0 && numOfOne == 1) {
                return true;
            }
        } while (++index <= 31);
        return true;
    }

    /**
     * 位运算替代求余： 计算 x % y 的值， y始终是2的n次方
     *
     * @param x
     * @param y 2^n (n>=0)
     * @return
     */
    public static int mod(int x, int y) {
        return x & (y - 1);
    }

}
