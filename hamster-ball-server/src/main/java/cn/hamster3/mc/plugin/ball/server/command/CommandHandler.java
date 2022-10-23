package cn.hamster3.mc.plugin.ball.server.command;

import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class CommandHandler {
    public static final CommandHandler INSTANCE = new CommandHandler();

    private static final Logger LOGGER = LoggerFactory.getLogger("command");
    private NioEventLoopGroup loopGroup;

    private boolean started;

    public void start(NioEventLoopGroup loopGroup) {
        this.loopGroup = loopGroup;

        started = true;
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("命令执行器准备就绪. 输入 help 查看命令帮助.");
        while (started) {
            String command = scanner.nextLine();
            try {
                executeCommand(command);
            } catch (Exception e) {
                LOGGER.error("执行命令 " + command + " 时遇到了一个异常: ", e);
            }
        }
    }

    public void executeCommand(String command) throws Exception {
        String[] args = command.split(" ");
        switch (args[0].toLowerCase()) {
            case "?":
            case "help": {
                help();
                break;
            }
            case "end":
            case "stop": {
                stop();
                break;
            }
            default: {
                LOGGER.info("未知指令. 请输入 help 查看帮助.");
                break;
            }
        }
    }

    public void help() {
        LOGGER.info("===============================================================");
        LOGGER.info("help                            - 查看帮助.");
        LOGGER.info("stop                            - 关闭该程序.");
        LOGGER.info("===============================================================");
    }

    public void stop() throws Exception {
        started = false;
        LOGGER.info("准备关闭服务器...");
        loopGroup.shutdownGracefully().await();
        LOGGER.info("服务器已关闭!");
    }

}
