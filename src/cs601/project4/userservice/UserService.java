package cs601.project4.userservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import cs601.project4.model.UserConfig;

public class UserService {
	public static void main(String[] args) throws Exception {
		Server server = new Server(UserConfig.getInstance().getAppPort());
		ServletContextHandler handler = new ServletContextHandler();
		server.setHandler(handler);
		
		handler.addServlet(CreateUserHandler.class, "/create");
		handler.addServlet(PurchaseTicketsHandler.class, "/tickets/add");
		handler.addServlet(TransferTicketsHandler.class, "/tickets/transfer");
		handler.addServlet(GetUserHandler.class, "/*");
		
		server.start();
		server.join();
	}
}
