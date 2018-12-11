package cs601.project4.eventservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs601.project4.model.EventConfig;

public class EventService {
	public static void main(String[] args) throws Exception {
		Server server = new Server(EventConfig.getInstance().getAppPort());
		ServletContextHandler handler = new ServletContextHandler();
		server.setHandler(handler);
		
		handler.addServlet(CreateEventHandler.class, "/create");
		handler.addServlet(ListEventsHandler.class, "/list");
		handler.addServlet(PurchaseEventTicketsHandler.class, "/purchase/*");
		handler.addServlet(GetEventHandler.class, "/*");
		
		server.start();
		server.join();
	}
}
