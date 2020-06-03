package com.cn.designmodel.bridge;

/**
 * App
 *
 * @author lvbiao8
 * @date 2020-06-03 20:04
 */
public class App {
    public static void main(String[] args) {
        SpaceMove move = new NormalSpaceMove();
        move.ahead(1);
        move.back(1);
        move.left(1);
        move.right(1);
        move.jump(1);
    }
}
