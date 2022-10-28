package cn.hamster3.mc.plugin.core.bungee;

import cn.hamster3.mc.plugin.core.bungee.api.BallBungeeCordAPI;
import cn.hamster3.mc.plugin.core.bungee.listener.BallBungeeCordListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class HamsterBallPlugin extends Plugin {
    private static HamsterBallPlugin instance;

    public static HamsterBallPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        Logger logger = getLogger();
        BallBungeeCordAPI.init();
        logger.info("BallBungeeCordAPI 已初始化.");
        try {
            BallBungeeCordAPI.getInstance().enable();
            logger.info("BallBungeeCordAPI 已启动.");
        } catch (Exception e) {
            e.printStackTrace();
            ProxyServer.getInstance().stop();
        }
    }

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        ProxyServer.getInstance().getPluginManager().registerListener(this, BallBungeeCordListener.INSTANCE);
        logger.info("已注册 BallBungeeCordListener.");
        logger.info("HamsterBall 已启动.");
    }

    @Override
    public void onDisable() {
        try {
            BallBungeeCordAPI.getInstance().disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
