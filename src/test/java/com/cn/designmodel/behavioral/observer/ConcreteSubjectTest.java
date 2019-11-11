package com.cn.designmodel.behavioral.observer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConcreteSubjectTest {

    @Test
    public void registerObserver() {
        ConcreteSubject concreteSubject = new ConcreteSubject();
        ConcreteObserver concreteObserver = new ConcreteObserver();
        concreteSubject.registerObserver(concreteObserver);
        concreteSubject.notifyObserver("hello world");
    }
}