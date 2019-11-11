package com.cn.designmodel.behavioral.observer.jdk;

import java.util.Observable;
import java.util.Observer;

/**
 * @author: lvbiao
 * @date: 2019-11-11 14:33
 */
public class MyObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("收到关心的消息：" + String.valueOf(arg));
    }
}
