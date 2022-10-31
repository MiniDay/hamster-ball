package cn.hamster3.mc.plugin.ball.common.connector;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class BallKeepAliveHandler extends ChannelInboundHandlerAdapter {
    public static final BallKeepAliveHandler INSTANCE = new BallKeepAliveHandler();

    private BallKeepAliveHandler() {
        super();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception {
        if (event instanceof IdleStateEvent) {
            context.channel().writeAndFlush("ping");
            return;
        }
        super.userEventTriggered(context, event);
    }
}
