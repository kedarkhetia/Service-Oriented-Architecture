package cs601.project4.eventservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import cs601.project4.model.EventConfig;
import cs601.project4.model.UserConfig;

public class EventService {
	public static void main(String[] args) throws Exception {
		Server server = new Server(EventConfig.getInstance().getAppPort());
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		
		handler.addServletWithMapping(CreateEventHandler.class, "/create");
		handler.addServletWithMapping(ListEventsHandler.class, "/list");
		handler.addServletWithMapping(GetEventHandler.class, "/*");
		
		server.start();
		server.join();
	}
}
