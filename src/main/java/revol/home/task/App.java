package revol.home.task;

import org.eclipse.jetty.server.Server;

public class App {
    private final static int SERVER_PORT = 8081;

    private static Server createServer() {
        Server server = new Server(SERVER_PORT);
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = createServer();
        try {
            server.start();
            server.join();
        } catch (Exception ignore) {

        } finally {
            server.stop();
        }

    }
}
