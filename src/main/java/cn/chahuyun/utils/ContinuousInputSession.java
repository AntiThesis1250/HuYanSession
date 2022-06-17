package cn.chahuyun.utils;

import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * ContinuousInputSession
 *
 * @author Zhangjiaxing
 * @description 用于获取用户下一次对话的监听
 * @date 2022/6/17 9:04
 */
public class ContinuousInputSession {

    private static EventChannel<MessageEvent> MessageEvent;

    static {
        //消息监听器 监听 2061954151 的所有消息
        MessageEvent = GlobalEventChannel.INSTANCE.filterIsInstance(MessageEvent.class)
                        .filter(event -> event.getBot().getId() == 2061954151L);
    }

    public static EventChannel<net.mamoe.mirai.event.events.MessageEvent> getMessageEvent() {
        return MessageEvent;
    }

    public static void setMessageEvent(EventChannel<net.mamoe.mirai.event.events.MessageEvent> messageEvent) {
        MessageEvent = messageEvent;
    }
}