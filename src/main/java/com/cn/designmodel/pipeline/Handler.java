package com.cn.designmodel.pipeline;

/**
 * Handler
 *
 * @author lvbiao8
 * @date 2020-05-22 15:03
 */
public interface Handler<I, O> {
    O process(I input);
}
