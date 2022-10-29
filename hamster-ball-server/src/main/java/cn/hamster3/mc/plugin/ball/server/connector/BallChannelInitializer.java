package cn.hamster3.mc.plugin.ball.server.connector;

import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.server.config.ServerConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BallChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    public static final BallChannelInitializer INSTANCE = new BallChannelInitializer();
    public static final List<Channel> CHANNELS = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger("BallServerCentre");

    private BallChannelInitializer() {
    }

    public static void broadcastMessage(BallMessageInfo messageInfo) {
        String string = messageInfo.toString();
        for (Channel channel : CHANNELS) {
            channel.writeAndFlush(string);
        }
    }

    @Override
    protected void initChannel(@NotNull NioSocketChannel channel) {
        LOGGER.info("远程地址 {} 请求建立连接.", channel.remoteAddress().toString());

        String hostAddress = channel.remoteAddress().getAddress().getHostAddress();
        if (ServerConfig.isEnableAcceptList() && !ServerConfig.getAcceptList().contains(hostAddress)) {
            channel.disconnect();
            LOGGER.warn("{} 不在白名单列表中, 已断开连接!", hostAddress);
            return;
        }

        channel.pipeline()
                .addLast(new LengthFieldPrepender(8))
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8))
                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                .addLast(new StringEncoder(StandardCharsets.UTF_8))
                .addLast(new BallChannelHandler());

        CHANNELS.add(channel);
    }

}
