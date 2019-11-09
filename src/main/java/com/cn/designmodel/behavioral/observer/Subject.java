package com.cn.designmodel.behavioral.observer;

/**
 * @author: lvbiao
 * @date: 2019-11-09 15:08
 */
public interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObserver(Object message);
}
