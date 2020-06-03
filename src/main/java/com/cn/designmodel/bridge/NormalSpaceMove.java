package com.cn.designmodel.bridge;

/**
 * NormalSpaceMove
 *
 * @author lvbiao8
 * @date 2020-06-03 20:03
 */
public class NormalSpaceMove extends NormalMove implements SpaceMove{
    @Override
    public void jump(int distance) {
        System.out.println("正常向上跳动" + distance);
    }
}
