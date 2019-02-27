package com.cn.offer;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-02-27 22:06
 * 用两个栈来实现一个队列，完成队列的Push和Pop操作。 队列中的元素为int类型。
 * 思路： 用stack1作为入队列，stack2作为出队列，当stack2为空时，将stack1出栈入到stack2中
 */
public class Stack2Queue {
    Stack<Integer> stack1 = new Stack<>();
    Stack<Integer> stack2 = new Stack<>();

    public static void main(String[] args) {
        Stack2Queue queue = new Stack2Queue();
        for (int i = 0; i < 4; i++) {
            queue.push(i);
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(queue.pop());
        }
    }

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        if (stack1.empty() && stack2.empty()) {
            throw new NoSuchElementException();
        }

        if (stack2.empty()) {
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }
}
