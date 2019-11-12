package com.cn.designmodel.create.factory.simple;

import org.junit.Test;

public class FactoryTest {

    @Test
    public void createProductA() {
        Product a = Factory.createProduct("A");
        a.work();
    }

    @Test
    public void createProductB() {
        Product b = Factory.createProduct("B");
        b.work();
    }
}