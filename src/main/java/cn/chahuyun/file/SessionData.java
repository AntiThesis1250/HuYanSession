package cn.chahuyun.file;

import cn.chahuyun.data.SessionDataBase;
import cn.chahuyun.enumerate.DataEnum;
import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginData;

import java.util.ArrayList;
import java.util.List;

/**
 * SessionData
 * 对话消息数据
 *
 * @author Zhangjiaxing
 * @description
 * @date 2022/6/16 14:25
 */
public class SessionData extends JavaAutoSavePluginData{

    /**
     * 唯一构造
     */
    public static final SessionData INSTANCE = new SessionData();

    /**
     * 文件名
     */
    public SessionData() {
        super("SessionData");
    }

    public final Value<String> string = value("测试","值");

    /**
     * list<SessionDataBase> 对话数据集合
     */
    public final Value<List<SessionDataBase>> sessionList = typedValue(
            "sessionList",
            createKType(List.class, createKType(SessionDataBase.class)),
            new ArrayList<SessionDataBase>(){
                        {
                            add(new SessionDataBase("乒", 0, "乓", null, DataEnum.ACCURATE));
                        }
                }
            );

}