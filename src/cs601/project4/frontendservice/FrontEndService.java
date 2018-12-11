package cs601.project4.frontendservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs601.project4.model.FrontEndConfig;
/**
 * Driver class that implements the server
 * 
 * @author kmkhetia
 *
 */
public class FrontEndService {
	public static void main(String args[]) throws Exception {
		Server server = new Server(FrontEndConfig.getInstance().getAppPort());
		ServletContextHandler handler = new ServletContextHandler();
		server.setHandler(handler);
		
		handler.addServlet(CreateEventHandler.class, "/events/create");
		handler.addServlet(ListEventHandler.class, "/events");
		handler.addServlet(GetEventHandler.class, "/events/*");
		handler.addServlet(CreateUserHandler.class, "/users/create");
		handler.addServlet(GetUserHandler.class, "/users/*");
		
		server.start();
		server.join();
	}
}
