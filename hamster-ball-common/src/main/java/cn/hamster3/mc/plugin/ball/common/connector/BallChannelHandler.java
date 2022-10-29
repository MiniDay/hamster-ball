package cn.hamster3.mc.plugin.ball.common.connector;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@ChannelHandler.Sharable
public class BallChannelHandler extends SimpleChannelInboundHandler<String> {
    public static final BallChannelHandler INSTANCE = new BallChannelHandler();

    private BallChannelHandler() {
        super(true);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String message) {
        if ("pong".equals(message)) {
            return;
        }
        BallMessageInfo info = CoreConstantObjects.GSON.fromJson(message, BallMessageInfo.class);
        for (BallListener listener : BallAPI.getInstance().getListeners()) {
            try {
                listener.onMessageReceived(info);
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
        if (!BallAPI.BALL_CHANNEL.equals(info.getChannel())) {
            return;
        }
        switch (info.getAction()) {
            case BallPlayerPreLoginEvent.ACTION: {
                BallPlayerPreLoginEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerPreLoginEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerPreLogin(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerLoginEvent.ACTION: {
                BallPlayerLoginEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerLoginEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerLogin(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerPostLoginEvent.ACTION: {
                BallPlayerPostLoginEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerPostLoginEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerPostLogin(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerPreConnectServerEvent.ACTION: {
                BallPlayerPreConnectServerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerPreConnectServerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerPreConnectServer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerConnectServerEvent.ACTION: {
                BallPlayerConnectServerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerConnectServerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerConnectServer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerPostConnectServerEvent.ACTION: {
                BallPlayerPostConnectServerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerPostConnectServerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerPostConnectServer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerLogoutEvent.ACTION: {
                BallPlayerLogoutEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerLogoutEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerLogout(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case BallPlayerChatEvent.ACTION: {
                BallPlayerChatEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BallPlayerChatEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBallPlayerChat(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case ServerOfflineEvent.ACTION: {
                ServerOfflineEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), ServerOfflineEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onServerOffline(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case ServerOnlineEvent.ACTION: {
                ServerOnlineEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), ServerOnlineEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onServerOnline(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext context) {
        BallAPI.getInstance().getLogger().warning("与服务器 " + context.channel().remoteAddress() + " 的连接已可用.");
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext context) {
        context.close();
        BallAPI.getInstance().getLogger().warning("与服务器 " + context.channel().remoteAddress() + " 的连接已断开.");
        for (BallListener listener : BallAPI.getInstance().getListeners()) {
            try {
                listener.onConnectInactive();
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
        BallAPI.getInstance().reconnect(5);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        BallAPI.getInstance().getLogger().log(Level.WARNING, "与服务器 " + context.channel().remoteAddress() + " 通信时出现了一个错误: ", cause);
        for (BallListener listener : BallAPI.getInstance().getListeners()) {
            try {
                listener.onConnectException(cause);
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
    }

}
