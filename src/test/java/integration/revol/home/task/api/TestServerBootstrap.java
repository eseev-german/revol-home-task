package integration.revol.home.task.api;

import io.restassured.RestAssured;
import org.eclipse.jetty.server.Server;
import revol.home.task.server.ServerBootstrap;

public class TestServerBootstrap {
    private final static int DEFAULT_TEST_PORT = 8081;
    private final static String APPLICATION_CONFIG_PATH = "integration.revol.home.task.config.TestContainerConfig";

    public static Server createServer() {
        int port = getServerPort();
        RestAssured.port = port;
        return new ServerBootstrap(port, APPLICATION_CONFIG_PATH).createServer();
    }

    private static int getServerPort() {
        String testPort = System.getProperty("testPort");
        if (testPort != null && !testPort.isBlank()) {
            return Integer.parseInt(testPort);
        }
        return DEFAULT_TEST_PORT;
    }
}