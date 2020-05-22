package com.cn.designmodel.pipeline;

/**
 * RemoveNumHandler
 *
 * @author lvbiao8
 * @date 2020-05-22 15:25
 */
public class RemoveBHandler implements Handler<String, String> {

    @Override
    public String process(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c != 'b' && c != 'B') {
                sb.append(c);
            }
        }
        sb.append("-B");
        return sb.toString();
    }
}
