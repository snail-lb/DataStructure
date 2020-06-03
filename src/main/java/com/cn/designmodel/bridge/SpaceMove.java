package com.cn.designmodel.bridge;

/**
 * SpaceMove
 * 主要功能接口的功能扩展
 * @author lvbiao8
 * @date 2020-06-03 19:59
 */
public interface SpaceMove extends Move{
    /**
     * 跳
     * @param distance
     */
    void jump(int distance);
}
