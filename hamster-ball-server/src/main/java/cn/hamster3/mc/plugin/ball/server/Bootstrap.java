package cn.hamster3.mc.plugin.ball.server;

import cn.hamster3.mc.plugin.ball.server.command.CommandHandler;
import cn.hamster3.mc.plugin.ball.server.config.ServerConfig;
import cn.hamster3.mc.plugin.ball.server.connector.BallServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Bootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger("Bootstrap");

    public static void main(String[] args) throws IOException {
        if (initDefaultFile()) {
            System.out.println("请重新启动该程序.");
            return;
        }
        ServerConfig.init();
        LOGGER.info("配置文件加载完成.");

        NioEventLoopGroup loopGroup = new NioEventLoopGroup(ServerConfig.getNioThread());
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(loopGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(BallServerChannelInitializer.INSTANCE);
        ChannelFuture channelFuture = bootstrap.bind(ServerConfig.getHost(), ServerConfig.getPort());
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                LOGGER.info("服务器已启动. 输入 stop 来关闭该程序.");
            } else {
                LOGGER.error("仓鼠球服务器启动失败!", future.cause());
                loopGroup.shutdownGracefully();
            }
        });

        CommandHandler.INSTANCE.start(loopGroup);
    }

    private static boolean initDefaultFile() throws IOException {
        boolean saved = false;
        File log4jFile = new File("log4j2.xml");
        if (!log4jFile.exists()) {
            InputStream stream = ServerConfig.class.getResourceAsStream("/log4j2.xml");
            if (stream == null) {
                throw new IOException("log4j2.xml 文件损坏!");
            }
            Files.copy(stream, log4jFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("已生成默认 log4j2.xml 文件!");
            saved = true;
        }

        File configFile = new File("config.yml");
        if (!configFile.exists()) {
            InputStream stream = ServerConfig.class.getResourceAsStream("/config.yml");
            if (stream == null) {
                throw new IOException("config.yml 文件损坏!");
            }
            Files.copy(stream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("已生成默认 config.yml 文件!");
            saved = true;
        }
        return saved;
    }
}
