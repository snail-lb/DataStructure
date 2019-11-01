package com.cn.designmodel.factory.simple;

import org.junit.Test;

import static org.junit.Assert.*;

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