package cn.hamster3.mc.plugin.ball.bukkit;

import cn.hamster3.mc.plugin.ball.bukkit.api.BallBukkitAPI;
import cn.hamster3.mc.plugin.ball.bukkit.hook.PlaceholderHook;
import cn.hamster3.mc.plugin.ball.bukkit.listener.BallBukkitListener;
import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class HamsterBallPlugin extends JavaPlugin {
    private static HamsterBallPlugin instance;

    public static HamsterBallPlugin getInstance() {
        return instance;
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(instance, runnable);
    }

    @Override
    public void onLoad() {
        instance = this;
        Logger logger = getLogger();
        BallBukkitAPI.init();
        logger.info("BallBukkitAPI 已初始化.");
        try {
            BallBukkitAPI.getInstance().enable();
            logger.info("BallBukkitAPI 已启动.");
        } catch (Exception e) {
            e.printStackTrace();
            sync(Bukkit::shutdown);
        }
    }

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        Bukkit.getPluginManager().registerEvents(BallBukkitListener.INSTANCE, this);
        logger.info("已注册 BallBukkitListener.");
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                ServerOnlineEvent.ACTION,
                new ServerOnlineEvent(BallAPI.getInstance().getLocalServerInfo())
        );
        logger.info("HamsterBall 已启动.");
        sync(() -> {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                PlaceholderHook.INSTANCE.register();
                logger.info("已挂载 PlaceholderAPI 变量!");
            } else {
                logger.warning("服务器未安装 PlaceholderAPI, 取消挂载变量!");
            }
        });
    }

    @Override
    public void onDisable() {
        try {
            BallBukkitAPI.getInstance().disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
