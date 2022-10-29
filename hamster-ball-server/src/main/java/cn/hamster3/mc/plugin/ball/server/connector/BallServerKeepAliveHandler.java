package cn.hamster3.mc.plugin.ball.server.connector;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class BallServerKeepAliveHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {
    public static final BallServerKeepAliveHandler INSTANCE = new BallServerKeepAliveHandler();

    private static final Logger LOGGER = LoggerFactory.getLogger("BallServerKeepAliveHandler");

    private BallServerKeepAliveHandler() {
        super(true);
    }

    @Override
    protected void eventReceived(ChannelHandlerContext context, IdleStateEvent event) {
        context.close();
        synchronized (BallServerChannelInitializer.CHANNELS) {
            BallServerChannelInitializer.CHANNELS.remove(context.channel());
        }
        LOGGER.warn("由于无法验证连接存活，与服务器 {} 的连接已断开.", context.channel().remoteAddress());
    }
}
