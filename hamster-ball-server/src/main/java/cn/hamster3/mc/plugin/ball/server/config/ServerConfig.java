package cn.hamster3.mc.plugin.ball.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public final class ServerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("ServerConfig");

    private static String host;
    private static int port;
    private static int nioThread;
    private static boolean enableAcceptList;
    private static List<String> acceptList;

    private ServerConfig() {
    }

    @SuppressWarnings("unchecked")
    public static void init() throws IOException {
        File configFile = new File("config.yml");
        InputStream stream = Files.newInputStream(configFile.toPath());
        Map<String, Object> map = new Yaml().load(stream);
        stream.close();

        host = (String) map.get("host");
        port = (int) map.get("port");
        nioThread = (int) map.get("nio-thread");
        enableAcceptList = (boolean) map.get("enable-accept-list");
        acceptList = (List<String>) map.get("accept-list");

        LOGGER.info("host: {}", host);
        LOGGER.info("port: {}", port);
        LOGGER.info("nioThread: {}", nioThread);
        LOGGER.info("enableAcceptList: {}", enableAcceptList);
        LOGGER.info("acceptList: {}", acceptList);
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static int getNioThread() {
        return nioThread;
    }

    public static boolean isEnableAcceptList() {
        return enableAcceptList;
    }

    public static List<String> getAcceptList() {
        return acceptList;
    }
}
