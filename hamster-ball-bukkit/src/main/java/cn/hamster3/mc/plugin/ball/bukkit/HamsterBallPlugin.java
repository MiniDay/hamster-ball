package cn.hamster3.mc.plugin.ball.bukkit;

import cn.hamster3.mc.plugin.ball.bukkit.api.BallBukkitAPI;
import cn.hamster3.mc.plugin.ball.bukkit.listener.BallBukkitListener;
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
        logger.info("HamsterBall 已启动.");
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
