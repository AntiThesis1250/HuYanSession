package cn.chahuyun.Session.Criticaldialog;

import cn.chahuyun.data.SessionDataBase;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * SessionDialogue
 *
 * @author Zhangjiaxing
 * @description 关键词对话的消息触发
 * @date 2022/6/16 14:36
 */
public class SessionDialogue {

    /**
     * @description 传递消息监视和指定的关键词对话
     * @author zhangjiaxing
     * @param messageEvent 消息事件
     * @param sessionDataBase 对话类
     * @date 2022/6/16 15:17
     */
    public static void session(MessageEvent messageEvent,SessionDataBase sessionDataBase) {
        //type = 0 为string类回复
        if (sessionDataBase.getType() == 0) {
            messageEvent.getSubject().sendMessage(sessionDataBase.getValue());
        }

    }

}