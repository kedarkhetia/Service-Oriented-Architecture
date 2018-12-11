package cs601.project4.eventservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import cs601.project4.model.EventConfig;
/**
 * Driver class that implements the server
 * 
 * @author kmkhetia
 *
 */
public class EventService {
	private final static Logger log = LogManager.getLogger(EventService.class);
	
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
