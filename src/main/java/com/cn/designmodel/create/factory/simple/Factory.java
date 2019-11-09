package com.cn.designmodel.create.factory.simple;

/**
 * @author: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-11-01 14:13
 */
public class Factory {
    public static Product createProduct(String productType) {
        if ("A".equals(productType)) {
            return new ProductA();
        } else if ("B".equals(productType)) {
            return new ProductB();
        }
        return null;
    }
}
