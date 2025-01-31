package cn.chahuyun;

import cn.chahuyun.Session.Criticaldialog.SpecialDialogue;
import cn.chahuyun.data.ScopeInfo;
import cn.chahuyun.data.SessionDataBase;
import cn.chahuyun.enumerate.DataEnum;
import cn.chahuyun.data.SessionData;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.event.GlobalEventChannel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AutoSaveDataTest {

    public static class MockPlugin extends JavaPlugin {
        public static final MockPlugin INSTANCE = new MockPlugin();
        public MockPlugin() {
            super(new JvmPluginDescriptionBuilder("org.test.test", "1.0.0").build());
        }
    }

    @Before
    public void beforeTest() {
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal());
//        PluginManager.INSTANCE.loadPlugin(MockPlugin.INSTANCE);
        PluginManager.INSTANCE.enablePlugin(MockPlugin.INSTANCE);
    }


    @Test
    public void testData() {
        MockPlugin.INSTANCE.reloadPluginData(SessionData.INSTANCE);
        Map<String, SessionDataBase> sessionMap = SessionData.INSTANCE.getSessionMap();
//        SessionData.INSTANCE.setSessionMap("+",new SessionDataBase("乒", 0, "乓", DataEnum.ACCURATE, new ScopeInfo("全局", false, 0L)));

//        Map<String, SessionDataBase> map = SessionData.INSTANCE.getSessionMap();


//        assertEquals("乌拉",SessionData.INSTANCE.getSessionMap().get("乌拉"));
    }
}
