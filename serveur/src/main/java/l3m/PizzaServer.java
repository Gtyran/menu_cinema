package l3m;

import java.util.Arrays;
import l3m.Servlets.CommandeServlet;
import l3m.Servlets.ClientServlet;
import l3m.Servlets.BlockingServlet;
import l3m.Servlets.MenuServlet;
import l3m.Servlets.ClientAuthentificationServlet;
import javax.servlet.http.HttpServlet;
import l3m.Servlets.SuggestionFilmServlet;
import l3m.Servlets.SuggestionPlatServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class PizzaServer extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Server server;

    void start() throws Exception {
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(BlockingServlet.class, "/status");
        servletHandler.addServletWithMapping(ClientAuthentificationServlet.class, "/api/authentification");
        servletHandler.addServletWithMapping(ClientServlet.class, "/api/client");
        servletHandler.addServletWithMapping(CommandeServlet.class, "/api/commande");
        servletHandler.addServletWithMapping(MenuServlet.class, "/api/menu");
        servletHandler.addServletWithMapping(SuggestionPlatServlet.class, "/api/suggestions/plats");
        servletHandler.addServletWithMapping(SuggestionFilmServlet.class, "/api/suggestions/films");

        server.start();
    }

    void stop() throws Exception {
        //System.out.println("Server stop");
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        PizzaServer server = new PizzaServer();

        //Arrays.stream(args).forEach(System.out::println);

        server.start();
    }

}
