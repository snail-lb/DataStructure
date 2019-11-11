package com.cn.designmodel.behavioral.observer.jdk;

import org.junit.Test;

public class MyObserverTest {

    @Test
    public void update() {
        MyObserver myObserver = new MyObserver();
        MyObservable observable = new MyObservable();
        observable.addObserver(myObserver);
        observable.notifyObservers("hello world");
    }
}