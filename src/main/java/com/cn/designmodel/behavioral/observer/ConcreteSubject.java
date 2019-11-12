package com.cn.designmodel.behavioral.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: lvbiao
 * @date: 2019-11-09 15:11
 */
public class ConcreteSubject implements Subject {
    Set<Observer> observers = new HashSet<>();
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(Object message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
