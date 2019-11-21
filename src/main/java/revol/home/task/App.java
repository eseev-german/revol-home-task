package revol.home.task;

import revol.home.task.server.ServerBootstrap;

public class App {
    private final static int SERVER_PORT = 8081;
    private final static String APPLICATION_CONFIG_PATH = "revol.home.task.config.ContainerConfig";

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap(SERVER_PORT, APPLICATION_CONFIG_PATH);
        serverBootstrap.setUpServer();
    }
}