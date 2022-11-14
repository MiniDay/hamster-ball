package cn.hamster3.mc.plugin.core.bungee;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import cn.hamster3.mc.plugin.core.bungee.api.BallBungeeCordAPI;
import cn.hamster3.mc.plugin.core.bungee.listener.BallBungeeCordListener;
import cn.hamster3.mc.plugin.core.bungee.util.BallBungeeCordUtils;
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
        for (BallServerInfo serverInfo : BallAPI.getInstance().getAllServerInfo().values()) {
            if (serverInfo.getType() != BallServerType.GAME) {
                continue;
            }
            ProxyServer.getInstance().getServers().put(serverInfo.getId(), BallBungeeCordUtils.getServerInfo(serverInfo));
            HamsterBallPlugin.getInstance().getLogger().info("已添加子服 " + serverInfo.getId() + " 的接入点配置.");
        }
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                ServerOnlineEvent.ACTION,
                new ServerOnlineEvent(BallAPI.getInstance().getLocalServerInfo())
        );
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
