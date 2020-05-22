package com.cn.designmodel.pipeline;

/**
 * App
 *
 * @author lvbiao8
 * @date 2020-05-22 15:32
 */
public class App {
    public static void main(String[] args) {
        String input = "1a2b3c4d";
        Pipeline<String,String> pipeline = new Pipeline<>(new RemoveAHandler())
                .addHandler(new RemoveBHandler());
        String output = pipeline.execute(input);
        System.out.println(output);
    }
}
