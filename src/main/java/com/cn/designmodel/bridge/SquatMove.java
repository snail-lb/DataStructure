package com.cn.designmodel.bridge;

/**
 * 蹲下移动
 * 主要的功能接口的实现
 * @author lvbiao8
 * @date 2020-06-03 19:53
 */
public class SquatMove implements Move{
    @Override
    public void ahead(int distance) {
        System.out.println("蹲下向前移动" + distance);
    }

    @Override
    public void back(int distance) {
        System.out.println("蹲下向后移动" + distance);
    }

    @Override
    public void left(int distance) {
        System.out.println("蹲下向左移动" + distance);
    }

    @Override
    public void right(int distance) {
        System.out.println("蹲下向右移动" + distance);
    }
}
