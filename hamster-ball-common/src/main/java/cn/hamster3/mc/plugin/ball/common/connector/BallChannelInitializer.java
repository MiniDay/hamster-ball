package cn.hamster3.mc.plugin.ball.common.connector;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class BallChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    public static final BallChannelInitializer INSTANCE = new BallChannelInitializer();

    private BallChannelInitializer() {
    }

    @Override
    protected void initChannel(@NotNull NioSocketChannel channel) {
        channel.pipeline()
                .addLast(new LengthFieldPrepender(8))
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8))
                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                .addLast(new StringEncoder(StandardCharsets.UTF_8))
                .addLast(BallChannelInboundHandler.INSTANCE)
        ;
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext context) {
        for (BallListener listener : BallAPI.getInstance().getListeners()) {
            try {
                listener.onConnectInactive();
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        for (BallListener listener : BallAPI.getInstance().getListeners()) {
            try {
                listener.onConnectException(cause);
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
    }
}

