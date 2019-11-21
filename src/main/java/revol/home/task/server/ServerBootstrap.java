package revol.home.task.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class ServerBootstrap {
    private final int serverPort;
    private final String applicationConfigPath;

    public ServerBootstrap(int serverPort, String applicationConfigPath) {
        this.serverPort = serverPort;
        this.applicationConfigPath = applicationConfigPath;
    }

    public Server createServer() {
        Server server = new Server(serverPort);

        ServletContextHandler servletContextHandler = new ServletContextHandler();

        servletContextHandler.setContextPath("/");

        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                "javax.ws.rs.Application",
                applicationConfigPath
        );
        server.setHandler(servletContextHandler);
        return server;
    }

}
