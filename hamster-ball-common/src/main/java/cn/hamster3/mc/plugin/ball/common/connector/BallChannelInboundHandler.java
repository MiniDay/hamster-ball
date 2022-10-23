package cn.hamster3.mc.plugin.ball.common.connector;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.ServiceMessageInfo;
import cn.hamster3.mc.plugin.ball.common.event.operate.*;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BallChannelInboundHandler extends SimpleChannelInboundHandler<String> {
    public static final BallChannelInboundHandler INSTANCE = new BallChannelInboundHandler();

    private BallChannelInboundHandler() {
        super(true);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String message) {
        ServiceMessageInfo info = CoreConstantObjects.GSON.fromJson(message, ServiceMessageInfo.class);
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
            case BroadcastPlayerMessageEvent.ACTION: {
                BroadcastPlayerMessageEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), BroadcastPlayerMessageEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onBroadcastPlayerMessage(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case DispatchConsoleCommandEvent.ACTION: {
                DispatchConsoleCommandEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), DispatchConsoleCommandEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onDispatchConsoleCommand(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case DispatchPlayerCommandEvent.ACTION: {
                DispatchPlayerCommandEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), DispatchPlayerCommandEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onDispatchGamePlayerCommand(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case KickPlayerEvent.ACTION: {
                KickPlayerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), KickPlayerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onKickPlayer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case SendMessageToPlayerEvent.ACTION: {
                SendMessageToPlayerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), SendMessageToPlayerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onSendMessageToPlayer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case SendPlayerToLocationEvent.ACTION: {
                SendPlayerToLocationEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), SendPlayerToLocationEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onSendPlayerToLocation(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case SendPlayerToPlayerEvent.ACTION: {
                SendPlayerToPlayerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), SendPlayerToPlayerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onSendPlayerToPlayer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerChatEvent.ACTION: {
                PlayerChatEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerChatEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerChat(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerConnectServerEvent.ACTION: {
                PlayerConnectServerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerConnectServerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerConnectServer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerDisconnectEvent.ACTION: {
                PlayerDisconnectEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerDisconnectEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerDisconnect(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerLoginEvent.ACTION: {
                PlayerLoginEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerLoginEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerLogin(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerPostConnectServerEvent.ACTION: {
                PlayerPostConnectServerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerPostConnectServerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerPostConnectServer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerPostLoginEvent.ACTION: {
                PlayerPostLoginEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerPostLoginEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerPostLogin(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerPreConnectServerEvent.ACTION: {
                PlayerPreConnectServerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerPreConnectServerEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerPreConnectServer(event);
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case PlayerPreLoginEvent.ACTION: {
                PlayerPreLoginEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), PlayerPreLoginEvent.class);
                for (BallListener listener : BallAPI.getInstance().getListeners()) {
                    try {
                        listener.onPlayerPreLogin(event);
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
}
