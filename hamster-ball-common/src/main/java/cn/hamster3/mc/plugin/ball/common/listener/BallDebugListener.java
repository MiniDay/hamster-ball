package cn.hamster3.mc.plugin.ball.common.listener;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public final class BallDebugListener extends BallListener {
    public static final BallDebugListener INSTANCE = new BallDebugListener();

    private BallDebugListener() {
    }

    @Override
    public ListenerPriority getPriority() {
        return ListenerPriority.MONITOR;
    }

    @Override
    public void onConnectActive() {
        BallAPI.getInstance().getLogger().info("连接已可用。");
    }

    @Override
    public void onConnectException(Throwable throwable) {
        BallAPI.getInstance().getLogger().log(Level.INFO, "连接出现错误！", throwable);
    }

    @Override
    public void onServiceDead() {
        BallAPI.getInstance().getLogger().info("已无法建立与服务中心的连接！");
    }

    @Override
    public void onBallPlayerPreLogin(@NotNull BallPlayerPreLoginEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerPreLoginEvent: " + event.getPlayerName());
    }

    @Override
    public void onBallPlayerLogin(@NotNull BallPlayerLoginEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerLoginEvent: " + event.getPlayerInfo().getName());
    }

    @Override
    public void onBallPlayerPostLogin(@NotNull BallPlayerPostLoginEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerPostLoginEvent: " + event.getPlayerInfo().getName());
    }

    @Override
    public void onBallPlayerPreConnectServer(@NotNull BallPlayerPreConnectServerEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerPreConnectServerEvent: ");
        BallAPI.getInstance().getLogger().info("player: " + event.getPlayerInfo().getName());
        BallAPI.getInstance().getLogger().info("from: " + event.getFrom());
        BallAPI.getInstance().getLogger().info("to: " + event.getTo());
    }

    @Override
    public void onBallPlayerConnectServer(@NotNull BallPlayerConnectServerEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerConnectServerEvent: ");
        BallAPI.getInstance().getLogger().info("player: " + event.getPlayerInfo().getName());
        BallAPI.getInstance().getLogger().info("from: " + event.getFrom());
        BallAPI.getInstance().getLogger().info("to: " + event.getTo());
    }

    @Override
    public void onBallPlayerPostConnectServer(@NotNull BallPlayerPostConnectServerEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerPostConnectServerEvent: ");
        BallAPI.getInstance().getLogger().info("player: " + event.getPlayerInfo().getName());
    }

    @Override
    public void onBallPlayerLogout(@NotNull BallPlayerLogoutEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerLogoutEvent: ");
        BallAPI.getInstance().getLogger().info("player: " + event.getPlayerInfo().getName());
    }

    @Override
    public void onBallPlayerChat(@NotNull BallPlayerChatEvent event) {
        BallAPI.getInstance().getLogger().info("BallPlayerChatEvent: ");
        BallAPI.getInstance().getLogger().info("displayName: " + event.getDisplayName());
        BallAPI.getInstance().getLogger().info("playerUUID: " + event.getPlayerUUID());
        BallAPI.getInstance().getLogger().info("message: " + PlainTextComponentSerializer.plainText().serialize(event.getMessage()));
    }

    @Override
    public void onServerOffline(@NotNull ServerOfflineEvent event) {
        BallAPI.getInstance().getLogger().info("服务器已离线: " + event.getServerID());
    }

    @Override
    public void onServerOnline(@NotNull ServerOnlineEvent event) {
        BallAPI.getInstance().getLogger().info("服务器已上线: " + event.getServerInfo().getId());
    }

    @Override
    public void onMessageSend(@NotNull BallMessageInfo event) {
        BallAPI.getInstance().getLogger().info("发送了一条消息: " + event);
    }

    @Override
    public void onMessageReceived(@NotNull BallMessageInfo event) {
        BallAPI.getInstance().getLogger().info("收到了一条消息: " + event);
    }
}
