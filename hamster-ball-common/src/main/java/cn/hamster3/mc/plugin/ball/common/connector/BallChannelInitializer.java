package cn.hamster3.mc.plugin.ball.common.connector;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class BallChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    public static final BallChannelInitializer INSTANCE = new BallChannelInitializer();

    private BallChannelInitializer() {
    }

    @Override
    protected void initChannel(@NotNull NioSocketChannel channel) {
        channel.pipeline()
                .addLast(new IdleStateHandler(0, 7, 0, TimeUnit.SECONDS))
                .addLast(BallKeepAliveHandler.INSTANCE)
                .addLast(new LengthFieldPrepender(8))
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8))
                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                .addLast(new StringEncoder(StandardCharsets.UTF_8))
                .addLast(BallChannelHandler.INSTANCE);
    }

}

