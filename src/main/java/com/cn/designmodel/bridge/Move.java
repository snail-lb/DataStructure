package com.cn.designmodel.bridge;

/**
 * 移动控制基础接口
 * 主要的功能接口
 *
 * 桥接模式是将类的功能层次结构和类的实现层次接口区分开并连接起来
 * Move是主要的功能接口,SpaceMove数据Move的功能扩展
 * NormalMove,SquatMove数据Move的功能实现
 * @author lvbiao8
 * @date 2020-06-03 19:48
 */
public interface Move {
    /**
     * 前进
     * @param distance
     */
    void ahead(int distance);

    /**
     * 后退
     * @param distance
     */
    void back(int distance);

    /**
     * 向左移动
     * @param distance
     */
    void left(int distance);

    /**
     * 向右移动
     * @param distance
     */
    void right(int distance);
}
