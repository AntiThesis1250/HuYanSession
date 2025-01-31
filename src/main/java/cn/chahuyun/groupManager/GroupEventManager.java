package cn.chahuyun.groupManager;

import cn.chahuyun.HuYanSession;
import cn.chahuyun.data.SessionData;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

/**
 * GroupManager
 *
 * @author Zhangjiaxing
 * @description 群事件捕获
 * @date 2022/6/21 8:44
 */
public class GroupEventManager{

    public static final GroupEventManager INSTANCE = new GroupEventManager();
    private MiraiLogger l = HuYanSession.INSTANCE.getLogger();


    /**
     * @description 群新加入成员欢迎
     * @author zhangjiaxing
     * @param event 新加成员事件
     * @date 2022/6/21 8:48
     * @return void
     */
    public void onMemberJoinEvent(@NotNull MemberJoinEvent event) {
        l.info(event.getMember().getId()+"("+event.getMember().getNameCard()+")" +"入群");
        //获取欢迎词
        Map<String, String> welcomeMessage = SessionData.INSTANCE.getGroupWelcomeMessage();
        //为空为默认欢迎词
        if (welcomeMessage.keySet().size() == 0) {
            event.getGroup().sendMessage("小茶壶欢迎带佬入群~~~");
            return;
        }
        //创建key数组
        String[] strings = welcomeMessage.keySet().toArray(new String[0]);
        //随机获取key
        Random random = new Random();
        String string = strings[random.nextInt(strings.length)];
        //获取value
        String s = welcomeMessage.get(string);
        //MiraiCode转换
        MessageChain messages = new MessageChainBuilder()
                .append(new At(event.getMember().getId()))
                .append(MiraiCode.deserializeMiraiCode(s))
                .build();
        event.getGroup().sendMessage(messages);
    }



}