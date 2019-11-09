package com.cn.designmodel.create.factory.factory;

import org.junit.Test;


public class FactoryTest {

    @Test
    public void createProductA() {
        Product a = new ProductAFactory().createProduct();
        a.work();
    }

    @Test
    public void createProductB() {
        Product b = new ProductBFactory().createProduct();
        b.work();
    }
}