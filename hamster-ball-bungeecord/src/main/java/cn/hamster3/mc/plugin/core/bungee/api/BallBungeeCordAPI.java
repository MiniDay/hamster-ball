package cn.hamster3.mc.plugin.core.bungee.api;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.config.BallConfig;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.core.bungee.HamsterBallPlugin;
import cn.hamster3.mc.plugin.core.bungee.listener.BallBungeeCordListener;
import cn.hamster3.mc.plugin.core.bungee.util.BungeeCordUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Optional;

public class BallBungeeCordAPI extends BallAPI {
    public BallBungeeCordAPI(@NotNull BallConfig config) {
        super(config);
    }

    public static BallBungeeCordAPI getInstance() {
        return (BallBungeeCordAPI) instance;
    }

    @SuppressWarnings("deprecation")
    public static void init() {
        if (instance != null) {
            return;
        }
        HamsterBallPlugin plugin = HamsterBallPlugin.getInstance();
        Configuration pluginConfig = BungeeCordUtils.getPluginConfig(plugin);
        Optional<InetSocketAddress> address = ProxyServer.getInstance().getConfig().getListeners().stream().findFirst().map(ListenerInfo::getHost);
        String host = pluginConfig.getString("server-info.name.host", address.map(InetSocketAddress::getHostName).orElse(""));
        BallConfig config = new BallConfig(
                new BallServerInfo(
                        pluginConfig.getString("server-info.id"),
                        pluginConfig.getString("server-info.name"),
                        BallServerType.GAME,
                        host.isEmpty() ? "127.0.0.1" : host,
                        pluginConfig.getInt("server-info.name.port", address.map(InetSocketAddress::getPort).orElse(25577))
                ),
                pluginConfig.getString("ball-server.host"),
                pluginConfig.getInt("ball-server.port"),
                pluginConfig.getInt("ball-server.nio-thread")
        );
        instance = new BallBungeeCordAPI(config);
        instance.addListener(BallBungeeCordListener.INSTANCE);
    }

    @Override
    public void enable() throws SQLException, InterruptedException {
        super.enable();
    }

    @Override
    public void disable() throws SQLException, InterruptedException {
        super.disable();
    }
}
