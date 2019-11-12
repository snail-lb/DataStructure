package com.cn.designmodel.behavioral.observer;

/**
 * @author: lvbiao
 * @date: 2019-11-09 15:07
 */
public class ConcreteObserver implements Observer {
    @Override
    public void update(Object message) {
        System.out.println("收到关心的消息：" + String.valueOf(message));
    }
}
