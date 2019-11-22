package revol.home.task.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.HashMap;

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
        HashMap<String, String> initParams = new HashMap<>();
        initParams.put(
                "jersey.config.server.provider.packages",
                "revol.home.task,org.glassfish.jersey.jackson");
        initParams.put(
                "javax.ws.rs.Application",
                applicationConfigPath
        );
        servletHolder.setInitParameters(initParams);
        server.setHandler(servletContextHandler);
        return server;
    }

}
