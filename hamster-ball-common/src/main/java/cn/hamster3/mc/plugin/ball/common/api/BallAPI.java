package cn.hamster3.mc.plugin.ball.common.api;

import cn.hamster3.mc.plugin.ball.common.config.BallConfig;
import cn.hamster3.mc.plugin.ball.common.connector.BallChannelInitializer;
import cn.hamster3.mc.plugin.ball.common.constant.BallCommonConstants;
import cn.hamster3.mc.plugin.ball.common.data.BallLocation;
import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallPlayerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.ball.common.event.operate.*;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import cn.hamster3.mc.plugin.ball.common.listener.ListenerPriority;
import cn.hamster3.mc.plugin.core.common.api.CoreAPI;
import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import cn.hamster3.mc.plugin.core.common.data.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public abstract class BallAPI {
    /**
     * API 使用的通信频道
     */
    public static final String BALL_CHANNEL = "HamsterBall";
    /**
     * API 实例
     */
    protected static BallAPI instance;
    @NotNull
    protected final ConcurrentHashMap<String, BallServerInfo> serverInfo;
    @NotNull
    protected final ConcurrentHashMap<UUID, BallPlayerInfo> playerInfo;

    @NotNull
    private final BallConfig config;
    @NotNull
    private final List<BallListener> listeners;

    private final Bootstrap bootstrap;
    private final NioEventLoopGroup executors;

    protected boolean enable;
    protected Channel channel;

    protected BallAPI(@NotNull BallConfig config) {
        this.config = config;
        executors = new NioEventLoopGroup(config.getNioThread());

        serverInfo = new ConcurrentHashMap<>();
        playerInfo = new ConcurrentHashMap<>();
        listeners = new ArrayList<>();

        bootstrap = new Bootstrap();
        bootstrap.group(executors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(BallChannelInitializer.INSTANCE);

        addListener(new BallListener() {
            @Override
            public ListenerPriority getPriority() {
                return ListenerPriority.LOW;
            }

            @Override
            public void onBallPlayerConnectServer(@NotNull BallPlayerConnectServerEvent event) {
                BallPlayerInfo info = event.getPlayerInfo();
                playerInfo.put(info.getUuid(), info);
            }

            @Override
            public void onBallPlayerLogout(@NotNull BallPlayerLogoutEvent event) {
                BallPlayerInfo info = event.getPlayerInfo();
                playerInfo.put(info.getUuid(), info);
            }

            @Override
            public void onBallPlayerLogin(@NotNull BallPlayerLoginEvent event) {
                BallPlayerInfo info = event.getPlayerInfo();
                playerInfo.put(info.getUuid(), info);
            }

            @Override
            public void onBallPlayerPostConnectServer(@NotNull BallPlayerPostConnectServerEvent event) {
                BallPlayerInfo info = event.getPlayerInfo();
                playerInfo.put(info.getUuid(), info);
            }

            @Override
            public void onBallPlayerPostLogin(@NotNull BallPlayerPostLoginEvent event) {
                BallPlayerInfo info = event.getPlayerInfo();
                playerInfo.put(info.getUuid(), info);
            }

            @Override
            public void onBallPlayerPreConnectServer(@NotNull BallPlayerPreConnectServerEvent event) {
                BallPlayerInfo info = event.getPlayerInfo();
                playerInfo.put(info.getUuid(), info);
            }

            @Override
            public void onServerOffline(@NotNull ServerOfflineEvent event) {
                String serverID = event.getServerID();
                serverInfo.remove(serverID);
            }

            @Override
            public void onServerOnline(@NotNull ServerOnlineEvent event) {
                BallServerInfo info = event.getServerInfo();
                serverInfo.put(info.getId(), info);
            }

            @Override
            public void onConnectInactive() {
                reconnect(5);
            }
        });
    }

    /**
     * 获取 API 实例
     *
     * @return API 实例
     */
    public static BallAPI getInstance() {
        return instance;
    }

    protected void enable() throws SQLException, InterruptedException {
        if (enable) {
            return;
        }
        enable = true;
        BallServerInfo localInfo = getLocalServerInfo();

        connect();

        try (Connection connection = CoreAPI.getInstance().getConnection()) {

            {
                Statement statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS " + BallCommonConstants.SQL.PLAYER_INFO_TABLE + "(" +
                        "`uuid` CHAR(36) PRIMARY KEY," +
                        "`name` VARCHAR(16) NOT NULL," +
                        "`game_server` VARCHAR(32) NOT NULL," +
                        "`proxy_server` VARCHAR(32) NOT NULL," +
                        "`online` BOOLEAN NOT NULL" +
                        ") CHARSET utf8mb4;");
                statement.execute("CREATE TABLE IF NOT EXISTS " + BallCommonConstants.SQL.SERVER_INFO_TABLE + "(" +
                        "`id` VARCHAR(32) PRIMARY KEY NOT NULL," +
                        "`name` VARCHAR(32) NOT NULL," +
                        "`type` VARCHAR(16) NOT NULL," +
                        "`host` VARCHAR(32) NOT NULL," +
                        "`port` INT NOT NULL" +
                        ") CHARSET utf8mb4;");
                statement.execute("CREATE TABLE IF NOT EXISTS " + BallCommonConstants.SQL.CACHED_MESSAGE_TABLE + "(" +
                        "`uuid` CHAR(36) NOT NULL," +
                        "`message` MEDIUMTEXT NOT NULL" +
                        ") CHARSET utf8mb4;");
                statement.close();
            }

            {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO " + BallCommonConstants.SQL.SERVER_INFO_TABLE + " VALUES(?, ?, ?, ?, ?);");
                statement.setString(1, localInfo.getId());
                statement.setString(2, localInfo.getName());
                statement.setString(3, localInfo.getType().name());
                statement.setString(4, localInfo.getHost());
                statement.setInt(5, localInfo.getPort());
                statement.executeUpdate();
                statement.close();
            }

            {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + BallCommonConstants.SQL.SERVER_INFO_TABLE + ";");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    String serverID = set.getString("id");
                    serverInfo.put(serverID, new BallServerInfo(
                            serverID,
                            set.getString("name"),
                            BallServerType.valueOf(set.getString("type")),
                            set.getString("host"),
                            set.getInt("port")
                    ));
                }
                set.close();
                statement.close();
            }

            {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + BallCommonConstants.SQL.PLAYER_INFO_TABLE + ";");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    UUID uuid = UUID.fromString(set.getString("uuid"));
                    playerInfo.put(uuid, new BallPlayerInfo(uuid,
                            set.getString("name"),
                            set.getString("game_server"),
                            set.getString("proxy_server"),
                            set.getBoolean("online")
                    ));
                }
                set.close();
                statement.close();
            }

        }

        sendBallMessage(
                BALL_CHANNEL,
                ServerOnlineEvent.ACTION,
                new ServerOnlineEvent(localInfo)
        );
    }

    protected void connect() throws InterruptedException {
        if (!enable) {
            return;
        }
        ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort()).await();
        if (future.isSuccess()) {
            channel = future.channel();
            for (BallListener listener : listeners) {
                listener.onConnectActive();
            }
        }
    }

    protected void reconnect(int ttl) {
        if (!enable) {
            return;
        }
        if (channel != null && channel.isOpen() && channel.isRegistered() && channel.isActive() && channel.isWritable()) {
            return;
        }
        channel = null;
        if (ttl <= 0) {
            for (BallListener listener : getListeners()) {
                try {
                    listener.onReconnectFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        try {
            connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (channel != null) {
            return;
        }
        reconnect(ttl - 1);
    }

    protected void disable() throws SQLException, InterruptedException {
        if (!enable) {
            return;
        }
        enable = false;

        sendBallMessage(
                BALL_CHANNEL,
                ServerOfflineEvent.ACTION,
                new ServerOfflineEvent(getLocalServerId())
        );

        try (Connection connection = CoreAPI.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + BallCommonConstants.SQL.SERVER_INFO_TABLE + " WHERE `id`=?;");
            statement.setString(1, getLocalServerId());
            statement.executeUpdate();
            statement.close();
        }

        channel = null;
        executors.shutdownGracefully().await();
    }

    /**
     * 判断该服务器信息是否为本服
     *
     * @param info 服务器信息
     * @return true 代表该服务器信息是本服服务器
     */
    public boolean isLocalServer(@NotNull BallServerInfo info) {
        return getLocalServerInfo().equals(info);
    }

    /**
     * 判断该服务器信息是否为本服
     *
     * @param serverID 服务器ID
     * @return true 代表该服务器信息是本服服务器
     */
    public boolean isLocalServer(@NotNull String serverID) {
        return getLocalServerId().equalsIgnoreCase(serverID);
    }

    /**
     * 给服务器的在线玩家广播一条消息
     *
     * @param message 消息
     */
    public void broadcastPlayerMessage(@NotNull String message) {
        broadcastPlayerMessage(new Message().message(message));
    }

    /**
     * 给服务器的在线玩家广播一条消息
     *
     * @param message 消息
     */
    public void broadcastPlayerMessage(@NotNull Message message) {
        sendBallMessage(
                BALL_CHANNEL,
                BallServerType.PROXY,
                BroadcastPlayerMessageEvent.ACTION,
                new BroadcastPlayerMessageEvent(message)
        );
    }

    /**
     * 强制控制台执行命令
     *
     * @param type     执行对象的服务端类型
     * @param serverID 执行对象的 ID
     * @param command  命令内容
     */
    public void dispatchConsoleCommand(@Nullable BallServerType type, @Nullable String serverID, @NotNull String command) {
        sendBallMessage(
                BALL_CHANNEL,
                BallServerType.GAME,
                DispatchConsoleCommandEvent.ACTION,
                new DispatchConsoleCommandEvent(type, serverID, command)

        );
    }

    /**
     * 强制玩家执行命令
     *
     * @param type    执行对象的服务端类型
     * @param uuid    执行对象的 UUID
     * @param command 命令内容
     */
    public void dispatchPlayerCommand(@Nullable BallServerType type, @Nullable UUID uuid, @NotNull String command) {
        sendBallMessage(
                BALL_CHANNEL,
                BallServerType.GAME,
                DispatchPlayerCommandEvent.ACTION,
                new DispatchPlayerCommandEvent(type, uuid, command)

        );
    }

    /**
     * 踢出玩家
     *
     * @param uuid   玩家
     * @param reason 原因
     */
    public void kickPlayer(@NotNull UUID uuid, @NotNull String reason) {
        kickPlayer(uuid, Component.text(reason));
    }

    /**
     * 踢出玩家
     *
     * @param uuid   玩家
     * @param reason 原因
     */
    public void kickPlayer(@NotNull UUID uuid, @NotNull Component reason) {
        sendBallMessage(
                BALL_CHANNEL,
                BallServerType.PROXY,
                KickPlayerEvent.ACTION,
                new KickPlayerEvent(uuid, reason)

        );
    }

    /**
     * 给玩家发送一条消息
     *
     * @param uuid    玩家
     * @param message 消息
     * @param cache   当玩家不在线时，是否缓存消息等待玩家上线再发送
     */
    public void sendMessageToPlayer(@NotNull UUID uuid, @NotNull Message message, boolean cache) {
        BallPlayerInfo info = getPlayerInfo(uuid);
        if (info == null || !info.isOnline()) {
            if (!cache) {
                return;
            }
            try (Connection connection = CoreAPI.getInstance().getConnection()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + BallCommonConstants.SQL.CACHED_MESSAGE_TABLE + " VALUES(?, ?);");
                statement.setString(1, uuid.toString());
                statement.setString(2, message.saveToJson().toString());
                statement.executeUpdate();
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        sendBallMessage(
                BALL_CHANNEL,
                BallServerType.PROXY,
                SendMessageToPlayerEvent.ACTION,
                new SendMessageToPlayerEvent(Collections.singleton(uuid), message)

        );
    }

    /**
     * 给玩家发送一条消息
     *
     * @param receiver 玩家
     * @param message  消息
     * @param cache    当玩家不在线时，是否缓存消息等待玩家上线再发送
     */
    public void sendMessageToPlayer(@NotNull Set<UUID> receiver, @NotNull Message message, boolean cache) {
        for (UUID uuid : receiver) {
            BallPlayerInfo info = getPlayerInfo(uuid);
            if (info == null || !info.isOnline()) {
                if (!cache) {
                    return;
                }
                try (Connection connection = CoreAPI.getInstance().getConnection()) {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO " + BallCommonConstants.SQL.CACHED_MESSAGE_TABLE + " VALUES(?, ?);");
                    statement.setString(1, uuid.toString());
                    statement.setString(2, message.saveToJson().toString());
                    statement.executeUpdate();
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        sendBallMessage(
                BALL_CHANNEL,
                BallServerType.PROXY,
                SendMessageToPlayerEvent.ACTION,
                new SendMessageToPlayerEvent(receiver, message)
        );
    }

    /**
     * 把玩家传送到一个位置
     * <p>
     * 如果目标位置不在当前服务器
     * <p>
     * 则会先尝试将玩家连接至目标服务器再进行传送
     *
     * @param sendPlayerUUID 玩家的uuid
     * @param location       坐标
     * @param doneMessage    传送完成后显示的消息
     */
    public void sendPlayerToLocation(@NotNull UUID sendPlayerUUID, @NotNull BallLocation location, @Nullable Message doneMessage) {
        sendBallMessage(
                BALL_CHANNEL,
                SendPlayerToLocationEvent.ACTION,
                new SendPlayerToLocationEvent(Collections.singleton(sendPlayerUUID), location, doneMessage)
        );
    }

    /**
     * 把玩家传送到一个位置
     * <p>
     * 如果目标位置不在当前服务器
     * <p>
     * 则会先尝试将玩家连接至目标服务器再进行传送
     *
     * @param sendPlayerUUID 玩家的uuid
     * @param location       坐标
     * @param doneMessage    传送完成后显示的消息
     */
    public void sendPlayerToLocation(@NotNull HashSet<UUID> sendPlayerUUID, @NotNull BallLocation location, @Nullable Message doneMessage) {
        sendBallMessage(
                BALL_CHANNEL,
                SendPlayerToLocationEvent.ACTION,
                new SendPlayerToLocationEvent(sendPlayerUUID, location, doneMessage)
        );
    }

    /**
     * 把玩家传送到另一个玩家身边
     * <p>
     * 支持跨服传送
     *
     * @param sendPlayerUUID 被传送的玩家
     * @param toPlayerUUID   传送的目标玩家
     */
    public void sendPlayerToPlayer(@NotNull UUID sendPlayerUUID, @NotNull UUID toPlayerUUID, @Nullable Message doneMessage, @Nullable Message doneTargetMessage) {
        sendBallMessage(
                BALL_CHANNEL,
                SendPlayerToPlayerEvent.ACTION,
                new SendPlayerToPlayerEvent(Collections.singleton(sendPlayerUUID), toPlayerUUID, doneMessage, doneTargetMessage)
        );
    }

    /**
     * 把玩家传送到另一个玩家身边
     * <p>
     * 支持跨服传送
     *
     * @param sendPlayerUUID 被传送的玩家
     * @param toPlayerUUID   传送的目标玩家
     * @param doneMessage    传送完成后显示的消息
     */
    public void sendPlayerToPlayer(@NotNull HashSet<UUID> sendPlayerUUID, @NotNull UUID toPlayerUUID, @Nullable Message doneMessage, @Nullable Message doneTargetMessage) {
        sendBallMessage(
                BALL_CHANNEL,
                SendPlayerToPlayerEvent.ACTION,
                new SendPlayerToPlayerEvent(sendPlayerUUID, toPlayerUUID, doneMessage, doneTargetMessage)
        );
    }

    /**
     * 发送一条服务消息
     *
     * @param channel 消息标签
     * @param action  执行动作
     */
    public void sendBallMessage(@NotNull String channel, @NotNull String action) {
        sendBallMessage(new BallMessageInfo(channel, getLocalServerId(), null, null, action, null));
    }

    /**
     * 发送一条有附加参数的服务消息
     *
     * @param channel 消息标签
     * @param action  执行动作
     * @param content 附加参数
     */
    public void sendBallMessage(@NotNull String channel, @NotNull String action, @NotNull String content) {
        sendBallMessage(new BallMessageInfo(channel, getLocalServerId(), null, null, action, new JsonPrimitive(content)));
    }

    /**
     * 发送一条有附加参数的消息
     *
     * @param channel 消息频道
     * @param action  执行动作
     * @param content 附加参数
     */
    public void sendBallMessage(@NotNull String channel, @NotNull String action, @NotNull JsonElement content) {
        sendBallMessage(new BallMessageInfo(channel, getLocalServerId(), null, null, action, content));
    }

    /**
     * 发送一条有附加参数的服务消息
     *
     * @param channel 消息标签
     * @param action  执行动作
     * @param content 附加参数
     */
    public void sendBallMessage(@NotNull String channel, @NotNull String action, @NotNull Object content) {
        sendBallMessage(new BallMessageInfo(channel, getLocalServerId(), null, null, action, CoreConstantObjects.GSON.toJsonTree(content)));
    }

    /**
     * 发送一条有附加参数的消息
     *
     * @param channel 消息频道
     * @param action  执行动作
     * @param content 附加参数
     */
    public void sendBallMessage(@NotNull String channel, @Nullable BallServerType receiverType, @NotNull String action, @NotNull JsonElement content) {
        sendBallMessage(new BallMessageInfo(channel, getLocalServerId(), null, receiverType, action, content));
    }

    /**
     * 发送一条有附加参数的服务消息
     *
     * @param channel 消息标签
     * @param action  执行动作
     * @param content 附加参数
     */
    public void sendBallMessage(@NotNull String channel, @Nullable BallServerType receiverType, @NotNull String action, @NotNull Object content) {
        sendBallMessage(new BallMessageInfo(channel, getLocalServerId(), null, receiverType, action, CoreConstantObjects.GSON.toJsonTree(content)));
    }

    /**
     * 发送自定义消息
     *
     * @param messageInfo 消息内容
     */
    public void sendBallMessage(@NotNull BallMessageInfo messageInfo) {
        sendBallMessage(messageInfo, false);
    }

    /**
     * 自定义服务消息信息并发送
     *
     * @param messageInfo 消息内容
     * @param block       是否阻塞（设置为 true 则必须等待消息写入网络的操作完成后，该方法才会退出）
     */
    public void sendBallMessage(@NotNull BallMessageInfo messageInfo, boolean block) {
        if (channel == null || !channel.isWritable()) {
            return;
        }
        ChannelFuture future = channel.write(CoreConstantObjects.GSON.toJsonTree(messageInfo));
        if (block) {
            try {
                future.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (BallListener listener : BallAPI.getInstance().getListeners()) {
            try {
                listener.onMessageSend(messageInfo);
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
    }

    @NotNull
    public List<Message> getCachedPlayerMessage(@NotNull UUID uuid) throws SQLException {
        ArrayList<Message> list = new ArrayList<>();
        try (Connection connection = CoreAPI.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT message FROM " + BallCommonConstants.SQL.CACHED_MESSAGE_TABLE + " WHERE `uuid`=?;");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                JsonObject object = CoreConstantObjects.JSON_PARSER.parse(set.getString("msg")).getAsJsonObject();
                list.add(new Message().json(object));
            }
            statement.close();
        }
        return list;
    }

    public void removeCachedPlayerMessage(@NotNull UUID uuid) throws SQLException {
        try (Connection connection = CoreAPI.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + BallCommonConstants.SQL.CACHED_MESSAGE_TABLE + " WHERE `uuid`=?;");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        }
    }

    public void addListener(@NotNull BallListener listener) {
        listeners.add(listener);
        listeners.sort(Comparator.comparing(BallListener::getPriority));
    }

    public void removeListener(@NotNull BallListener listener) {
        listeners.remove(listener);
    }

    /**
     * 获取本地服务器ID
     *
     * @return 服务器ID
     */
    @NotNull
    public BallServerInfo getLocalServerInfo() {
        return config.getLocalInfo();
    }

    @NotNull
    public String getLocalServerId() {
        return config.getLocalInfo().getId();
    }

    /**
     * 获取服务器信息
     *
     * @param serverID 服务器ID
     * @return 可能为 null
     */
    public BallServerInfo getServerInfo(@NotNull String serverID) {
        return serverInfo.get(serverID);
    }

    /**
     * 获取玩家信息
     *
     * @param uuid 玩家的 UUID
     * @return 玩家信息
     */
    public BallPlayerInfo getPlayerInfo(@NotNull UUID uuid) {
        return playerInfo.get(uuid);
    }

    /**
     * 获取玩家信息
     *
     * @param playerName 玩家名称
     * @return 玩家信息
     */
    public BallPlayerInfo getPlayerInfo(@NotNull String playerName) {
        return playerInfo.searchValues(Long.MAX_VALUE, info -> {
            if (info.getName().equalsIgnoreCase(playerName)) {
                return info;
            }
            return null;
        });
    }

    /**
     * 获取玩家名称
     *
     * @param uuid 玩家的 UUID
     * @return 如果数据不存在，则返回字符串形式的 "null"
     */
    @NotNull
    public String getPlayerName(@NotNull UUID uuid) {
        BallPlayerInfo info = getPlayerInfo(uuid);
        if (info == null) {
            return "null";
        }
        return info.getName();
    }

    @NotNull
    public ConcurrentHashMap<String, BallServerInfo> getAllServerInfo() {
        return serverInfo;
    }

    @NotNull
    public ConcurrentHashMap<UUID, BallPlayerInfo> getAllPlayerInfo() {
        return playerInfo;
    }

    @NotNull
    public List<BallListener> getListeners() {
        return listeners;
    }
}
