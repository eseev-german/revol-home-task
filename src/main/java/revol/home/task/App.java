package revol.home.task;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {
    private final static int SERVER_PORT = 8081;

    private static Server createServer() {
        Server server = new Server(SERVER_PORT);

        ServletContextHandler servletContextHandler = new ServletContextHandler();

        servletContextHandler.setContextPath("/");

        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.packages",
                "revol.home.task"
        );
        server.setHandler(servletContextHandler);
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
