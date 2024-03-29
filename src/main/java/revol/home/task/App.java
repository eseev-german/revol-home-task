package revol.home.task;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.server.ServerBootstrap;

public class App {
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    private final static int DEFAULT_SERVER_PORT = 8081;

    private final static String APPLICATION_CONFIG_PATH = "revol.home.task.config.ContainerConfig";

    public static void main(String[] args) {
        int port = getServerPort(args);
        ServerBootstrap serverBootstrap = new ServerBootstrap(port, APPLICATION_CONFIG_PATH);
        Server server = serverBootstrap.createServer();
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            LOG.error("Error occurred while server startup.", e);
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                LOG.error("Error occurred while server shutdown.", e);
            }
        }
    }

    private static int getServerPort(String[] args) {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        }
        return DEFAULT_SERVER_PORT;
    }
}