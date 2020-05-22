package com.cn.designmodel.pipeline;

/**
 * Pipeline
 *
 * @author lvbiao8
 * @date 2020-05-22 15:05
 */
public class Pipeline<I, O> {
    private final Handler<I, O> currentHandler;

    Pipeline(Handler<I, O> currentHandler) {
        this.currentHandler = currentHandler;
    }

    <K> Pipeline<I, K> addHandler(Handler<O, K> newHandler) {
        return new Pipeline<I, K>(input -> newHandler.process(currentHandler.process(input)));
    }

    O execute(I input) {
        return currentHandler.process(input);
    }

}
