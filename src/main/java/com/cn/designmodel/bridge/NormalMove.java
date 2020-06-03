package com.cn.designmodel.bridge;

/**
 * 正常移动
 * 主要的功能接口的实现
 * @author lvbiao8
 * @date 2020-06-03 19:53
 */
public class NormalMove implements Move{
    @Override
    public void ahead(int distance) {
        System.out.println("正常向前移动" + distance);
    }

    @Override
    public void back(int distance) {
        System.out.println("正常向后移动" + distance);
    }

    @Override
    public void left(int distance) {
        System.out.println("正常向左移动" + distance);
    }

    @Override
    public void right(int distance) {
        System.out.println("正常向右移动" + distance);
    }
}
