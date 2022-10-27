package cn.hamster3.mc.plugin.ball.server.connector;

import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.server.constant.ConstantObjects;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BallChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger("ChannelHandler");

    public BallChannelHandler() {
        super(true);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String message) {
        try {
            BallMessageInfo messageInfo = ConstantObjects.GSON.fromJson(message, BallMessageInfo.class);
            LOGGER.info("从服务器 {} 上收到一条消息: \n {}", messageInfo.getSenderID(), messageInfo);
            BallChannelInitializer.broadcastMessage(messageInfo);
        } catch (Exception e) {
            LOGGER.error(String.format("处理消息 %s 时出现错误: ", message), e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        context.close();
        BallChannelInitializer.CHANNELS.remove(context.channel());
        LOGGER.warn("与服务器 {} 的连接已断开.", context.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        LOGGER.warn("与服务器 {} 通信时出现了一个错误: ", context.channel().remoteAddress(), cause);
    }
}
