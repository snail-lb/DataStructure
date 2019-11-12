package com.cn.designmodel.behavioral.observer.jdk;

import java.util.Observable;

/**
 * @author: lvbiao
 * @date: 2019-11-11 14:34
 */
public class MyObservable extends Observable {
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
        super.clearChanged();
    }
}
