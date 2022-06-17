package cn.chahuyun.Session;

import cn.chahuyun.GroupSession;
import cn.chahuyun.data.SessionDataBase;
import cn.chahuyun.enumerate.DataEnum;
import cn.chahuyun.file.SessionData;
import cn.chahuyun.utils.ContinuousInputSession;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.ConcurrencyKind;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.EventPriority;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static cn.chahuyun.GroupSession.sessionData;

/**
 * 对话信息检测
 *
 * @author Zhangjiaxing
 * @description 检测对话是否合格
 * @date 2022/6/8 9:34
 */
public class SessionManage {

    private static MiraiLogger l = GroupSession.INSTANCE.getLogger();

    private static final int PAGE_SIZE = 5;


    /**
     * 学习正则
     */
    public static String studyPattern = "(学习 [\\w\\d\\u4e00-\\u9fa5]+ [\\w\\d\\u4e00-\\u9fa5]+( ?(精准|模糊|头部|结尾))?)";
    /**
     * 查询正则
     */
    public static String queryPattern = "((查询) ?([\\d\\w\\u4e00-\\u9fa5])*)";

    /**
     * @description 判断该消息是不是规定字符
     * @author zhangjiaxing
     * @param messageChain 消息链
     * @date 2022/6/8 12:32
     * @return boolean
     */
    public static boolean isString(MessageChain messageChain) {
        return false;
    }

    /**
     * @description 学习词汇的方法
     * @author zhangjiaxing
     * @param event 消息事件
     * @date 2022/6/16 20:56
     * @return boolean
     */
    public static boolean studySession(MessageEvent event) {
        String messageString = event.getMessage().contentToString();
        Contact subject = event.getSubject();

        //判断学习语法结构是否正确
        if (!Pattern.matches(studyPattern, messageString)) {
            subject.sendMessage("学习失败！学习结构应为：");
            subject.sendMessage("学习 (触发关键词) (回复内容) [精准|模糊|头部|结尾]");
            return false;
        }

        String[] strings = messageString.split(" ");
        l.info(Arrays.toString(strings));
        if (strings.length == 3 && !strings[2].equals("图片")) {
            //type = 0 为string类回复
            sessionData.sessionList.get().add(new SessionDataBase(strings[1],0,strings[2],null, DataEnum.ACCURATE));
            subject.sendMessage("学习成功! " + strings[1] + "->"+strings[2]);
            return true;
        }


        return false;
    }

    /**
     * @description 查询所有词汇的方法
     * @author zhangjiaxing
     * @param event 消息事件
     * @date 2022/6/16 20:57
     * @return boolean
     */
    public static boolean querySession(MessageEvent event) {
        String messageString = event.getMessage().contentToString();
        Contact subject = event.getSubject();

        //判断查询语法结构是否正确
        if (!Pattern.matches(queryPattern, messageString)) {
            subject.sendMessage("查询失败！查询结构应为：");
            subject.sendMessage("查询 [页数/关键词]");
            return false;
        }
        //当 消息仅为 查询 时 默认查询第一页消息
        if (messageString.length()<=3) {
            Map<String, Object> paramMap = new HashMap<String, Object>(){
                {
                    put("pageNum", 1);
                }
            };
            sessionMessageInInt(event,paramMap);
            return true;
        }
        //当消息 携带页数时
        String[] strings = messageString.split("");
        if (Pattern.matches("\\d", strings[1])) {
            l.info(strings.toString());
            Map<String, Object> paramMap = new HashMap<String, Object>(){
                {
                    put("pageNum", strings[1]);
                }
            };
            sessionMessageInInt(event,paramMap);
            return true;
        }else {
            ArrayList<SessionDataBase> session = (ArrayList<SessionDataBase>) sessionData.sessionList.get();
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.add("------查询关键词------\n");
            for (SessionDataBase base : session) {
                if (base.getType() == 0) {
                    if (base.getKey().contains(strings[1])) {
                            messageChainBuilder.add("\t"+ base.getKey()+" -> "+ base.getValue()+"\t:"+ base.getDataEnum().getType()+"\n");
                    }
                }
            }
            messageChainBuilder.add("------------------------");
            //发送消息
            event.getSubject().sendMessage(messageChainBuilder.build());
            return true;
        }
    }

    /**
     * @description 根据传递的数量来进行分页显示
     * @author zhangjiaxing
     * @param paramMap 参数 pageNum 当前页数
     * @param event 消息事件
     * @date 2022/6/16 22:08
     * @return java.lang.Boolean
     */
    private static Boolean sessionMessageInInt(MessageEvent event,Map<String, Object> paramMap) {
//        //判断参数是否携带页面大小信息
//        if (!paramMap.containsKey("pageSize")) {
//            paramMap.put("pageSize", PAGE_SIZE);
//        }
//        l.info("pageSize->" + paramMap.get("pageSize") + " pageNum->" + paramMap.get("pageNum"));
//        //进行分页
//        SessionDataPaging dataPaging = SessionDataPaging.queryPageInfo((int) paramMap.get("pageNum"),(int) paramMap.get("pageSize"), (ArrayList<SessionDataBase>) sessionData.getSession());
        //构造消息
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        messageChainBuilder.add("------所有关键词------\n");
        //循环添加内容
        ArrayList<SessionDataBase> list = (ArrayList<SessionDataBase>) sessionData.sessionList.get();
        for (SessionDataBase base : list) {
            messageChainBuilder.add("\t"+ base.getKey()+" -> "+ base.getValue()+"\t:"+ base.getDataEnum().getType()+"\n");
        }
//        messageChainBuilder.add("------- （"+dataPaging.getPageNum()+"/"+dataPaging.getPageAll()+"） -------");
        ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(event.getSubject());
        forwardMessageBuilder.add(event.getBot(),messageChainBuilder.build());
        //发送消息
        event.getSubject().sendMessage(messageChainBuilder.build());
        //创建下一条参数
//        paramMap.put("type", "页数");
//        paramMap.put("pageNum", dataPaging.getPageNum());
//        paramMap.put("pageAll", dataPaging.getPageAll());

//        nextEvent(event, paramMap);
        return true;

    }

    /**
     * @description 获取该用户的下一条消息
     * @author zhangjiaxing
     * @param messageEvent
     * @param map
     * @date 2022/6/17 10:07
     * @return void
     */
    private static void nextEvent(MessageEvent messageEvent, Map<String, Object> map) {
        EventChannel<MessageEvent> eventChannel = ContinuousInputSession.getMessageEvent();
        //过滤该群里面的该消息发送人
        CoroutineContext CoroutineContext = EmptyCoroutineContext.INSTANCE;
        eventChannel.filter(event -> event.getSubject() == messageEvent.getSubject())
                .filter(event -> event.getSender() == messageEvent.getSender())
                .subscribeOnce(MessageEvent.class,CoroutineContext,ConcurrencyKind.LOCKED,EventPriority.HIGH, event -> {
            nextMessage(event, messageEvent,map);
        });
    }


    /**
     * @description 进行页面访问设计
     * @author zhangjiaxing
     * @param nextMessageEvent
     * @param messageEvent
     * @param map
     * @date 2022/6/17 10:08
     * @return net.mamoe.mirai.event.events.MessageEvent
     */
     private static Boolean nextMessage(MessageEvent nextMessageEvent, MessageEvent messageEvent, Map<String, Object> map) {
         if ("页数".equals(map.get("type").toString())) {
             String nextMessage = nextMessageEvent.getMessage().serializeToMiraiCode();
             int pageNum = (int) map.get("pageNum");
             int pageAll = (int) map.get("pageAll");
             switch (nextMessage) {
                 case "下一页":
                     if (pageNum < pageAll) {
                         map.put("pageNum", pageNum + 1);
                         sessionMessageInInt(nextMessageEvent, map);
                     }
                     break;
                 case "上一页":
                     if (pageNum > 1) {
                         map.put("pageNum", pageNum - 1);
                         sessionMessageInInt(nextMessageEvent, map);
                     }
                     break;
                 case "首页":
                     map.put("pageNum", 1);
                     sessionMessageInInt(nextMessageEvent, map);
                     break;
                 case "尾页":
                     map.put("pageNum", pageAll);
                     sessionMessageInInt(nextMessageEvent, map);
                     break;
                 default:
                     break;
             }
         }
         return true;
    }

}