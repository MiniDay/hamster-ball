package cn.hamster3.mc.plugin.ball.common.connector;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleUserEventChannelHandler;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class BallKeepAliveHandler extends SimpleUserEventChannelHandler<IdleStateEvent> {
    public static final BallKeepAliveHandler INSTANCE = new BallKeepAliveHandler();

    private BallKeepAliveHandler() {
        super(true);
    }

    @Override
    protected void eventReceived(ChannelHandlerContext context, IdleStateEvent event) {
        context.channel().writeAndFlush("ping");
    }
}
