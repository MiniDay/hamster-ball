package cn.hamster3.mc.plugin.core.bungee.listener;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallPlayerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.ball.common.event.operate.*;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import cn.hamster3.mc.plugin.core.bungee.HamsterBallPlugin;
import cn.hamster3.mc.plugin.core.bungee.util.BallBungeeCordUtils;
import cn.hamster3.mc.plugin.core.common.api.CoreAPI;
import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import cn.hamster3.mc.plugin.core.common.data.DisplayMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public final class BallBungeeCordListener extends BallListener implements Listener {
    public static final BallBungeeCordListener INSTANCE = new BallBungeeCordListener();

    private BallBungeeCordListener() {
    }

    @Override
    public void onMessageReceived(@NotNull BallMessageInfo info) {
        switch (info.getAction()) {
            case BroadcastPlayerMessageEvent.ACTION: {
                BroadcastPlayerMessageEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BroadcastPlayerMessageEvent.class);
                DisplayMessage message = event.getMessage();
                Audience audience = CoreAPI.getInstance().getAudienceProvider().all();
                message.show(audience);
                break;
            }
            case DispatchConsoleCommandEvent.ACTION: {
                DispatchConsoleCommandEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), DispatchConsoleCommandEvent.class);
                if (event.getType() != null && event.getType() != BallServerType.PROXY) {
                    return;
                }
                if (event.getServerID() != null && !BallAPI.getInstance().isLocalServer(event.getServerID())) {
                    return;
                }
                ProxyServer server = ProxyServer.getInstance();
                server.getPluginManager().dispatchCommand(server.getConsole(), event.getCommand());
                break;
            }
            case DispatchPlayerCommandEvent.ACTION: {
                DispatchPlayerCommandEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), DispatchPlayerCommandEvent.class);
                if (event.getType() != null && event.getType() != BallServerType.GAME) {
                    return;
                }
                ProxyServer server = ProxyServer.getInstance();
                if (event.getUuid() != null) {
                    ProxiedPlayer player = server.getPlayer(event.getUuid());
                    if (player == null) {
                        return;
                    }
                    server.getPluginManager().dispatchCommand(player, event.getCommand());
                    return;
                }
                for (ProxiedPlayer player : server.getPlayers()) {
                    server.getPluginManager().dispatchCommand(player, event.getCommand());
                }
                break;
            }
            case KickPlayerEvent.ACTION: {
                KickPlayerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), KickPlayerEvent.class);
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getUuid());
                BaseComponent[] components = BungeeComponentSerializer.get().serialize(event.getReason());
                player.disconnect(components);
                break;
            }
            case SendMessageToPlayerEvent.ACTION: {
                SendMessageToPlayerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), SendMessageToPlayerEvent.class);
                for (UUID receiver : event.getReceivers()) {
                    Audience audience = CoreAPI.getInstance().getAudienceProvider().player(receiver);
                    event.getMessage().show(audience);
                }
                break;
            }
        }
    }

    @Override
    public void onServiceDead() {
        ProxyServer.getInstance().stop("HamsterBall 重连失败.");
    }

    @Override
    public void onServerOnline(@NotNull ServerOnlineEvent event) {
        BallServerInfo serverInfo = event.getServerInfo();
        if (serverInfo.getType() != BallServerType.GAME) {
            return;
        }
        ProxyServer.getInstance().getServers().put(serverInfo.getId(), BallBungeeCordUtils.getServerInfo(serverInfo));
        HamsterBallPlugin.getInstance().getLogger().info("已添加子服 " + serverInfo.getId() + " 的接入点配置.");
    }

    @Override
    public void onServerOffline(@NotNull ServerOfflineEvent event) {
        Map<String, ServerInfo> map = ProxyServer.getInstance().getServers();
        if (map.remove(event.getServerID()) != null) {
            HamsterBallPlugin.getInstance().getLogger().info("已移除子服 " + event.getServerID() + " 的接入点配置.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(PreLoginEvent event) {
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerPreLoginEvent.ACTION,
                new BallPlayerPreLoginEvent(event.getConnection().getName())
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(LoginEvent event) {
        if (event.isCancelled()) {
            return;
        }
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerLoginEvent.ACTION,
                new BallPlayerLoginEvent(new BallPlayerInfo(
                        event.getConnection().getUniqueId(),
                        event.getConnection().getName(),
                        "",
                        BallAPI.getInstance().getLocalServerId(),
                        true
                ))
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BallPlayerInfo playerInfo = BallBungeeCordUtils.getPlayerInfo(player, true);
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerPostLoginEvent.ACTION,
                new BallPlayerPostLoginEvent(playerInfo)
        );
        uploadPlayerInfo(playerInfo);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BallPlayerInfo playerInfo = BallBungeeCordUtils.getPlayerInfo(player, true);
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerConnectServerEvent.ACTION,
                new BallPlayerConnectServerEvent(playerInfo, playerInfo.getGameServer(), event.getTarget().getName())
        );
        uploadPlayerInfo(playerInfo);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BallPlayerInfo playerInfo = BallBungeeCordUtils.getPlayerInfo(player, true);
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerPostConnectServerEvent.ACTION,
                new BallPlayerPostConnectServerEvent(playerInfo)
        );
        uploadPlayerInfo(playerInfo);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerDisconnect(ServerDisconnectEvent event) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BallPlayerInfo playerInfo = BallBungeeCordUtils.getPlayerInfo(player, false);
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerLogoutEvent.ACTION,
                new BallPlayerLogoutEvent(playerInfo)
        );
        uploadPlayerInfo(playerInfo);
    }

    private void uploadPlayerInfo(BallPlayerInfo playerInfo) {
        ProxyServer.getInstance().getScheduler().runAsync(HamsterBallPlugin.getInstance(), () -> {
            try (Connection connection = CoreAPI.getInstance().getConnection()) {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO `hamster_ball_player_info` VALUES(?, ?, ?, ?, ?);");
                statement.setString(1, playerInfo.getUuid().toString());
                statement.setString(2, playerInfo.getName());
                statement.setString(3, playerInfo.getGameServer());
                statement.setString(4, playerInfo.getProxyServer());
                statement.setBoolean(5, playerInfo.isOnline());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
