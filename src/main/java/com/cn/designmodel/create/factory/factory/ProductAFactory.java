package com.cn.designmodel.create.factory.factory;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-11-01 14:38
 */
public class ProductAFactory implements Factory {
    public Product createProduct() {
        return new ProductA();
    }
}
