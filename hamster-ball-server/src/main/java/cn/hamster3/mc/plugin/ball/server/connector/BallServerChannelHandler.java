package cn.hamster3.mc.plugin.ball.server.connector;

import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.server.constant.ConstantObjects;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class BallServerChannelHandler extends SimpleChannelInboundHandler<String> {
    public static final BallServerChannelHandler INSTANCE = new BallServerChannelHandler();
    private static final Logger LOGGER = LoggerFactory.getLogger("BallServerChannelHandler");

    private BallServerChannelHandler() {
        super(true);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String message) {
        if ("ping".equals(message)) {
            context.writeAndFlush("pong");
            return;
        }
        try {
            BallMessageInfo messageInfo = ConstantObjects.GSON.fromJson(message, BallMessageInfo.class);
            LOGGER.info("从服务器 {} 上收到一条消息: {}", context.channel().remoteAddress(), messageInfo);
            BallServerChannelInitializer.broadcastMessage(messageInfo);
        } catch (Exception e) {
            LOGGER.error(String.format("处理消息 %s 时出现错误: ", message), e);
        }
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext context) {
        LOGGER.info("与服务器 {} 建立了连接.", context.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        context.close();
        synchronized (BallServerChannelInitializer.CHANNELS) {
            BallServerChannelInitializer.CHANNELS.remove(context.channel());
        }
        LOGGER.warn("与服务器 {} 的连接已断开.", context.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        LOGGER.warn("与服务器 {} 通信时出现了一个错误: ", context.channel().remoteAddress(), cause);
    }
}