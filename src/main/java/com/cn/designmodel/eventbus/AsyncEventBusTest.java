package com.cn.designmodel.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.*;

/**
 * EventBusTest
 *
 * @author lvbiao8
 * @date 2020-06-08 15:12
 */
public class AsyncEventBusTest {

    public static void main(String[] args) throws InterruptedException {
        // 创建事件总线
        EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());

        eventBus.register(new Object() {
            // 订阅字符串事件
            @Subscribe
            public void acceptStr(String str) {
                System.out.println("处理字符串事件:" + str);
            }

            // 订阅Event事件
            @Subscribe
            public void acceptEvent(Event event) {
                System.out.println("处理Event事件:" + event.key);
            }
        });

        // 推送字符串事件
        eventBus.post("字符串事件");
        // 推送event事件
        eventBus.post(Event.of("event事件"));
        eventBus.post("字符串事件2");
    }

    static class Event {
        String key;

        Event(String key) {
            this.key = key;
        }
        static Event of(String key) {
            return new Event(key);
        }
    }
}
