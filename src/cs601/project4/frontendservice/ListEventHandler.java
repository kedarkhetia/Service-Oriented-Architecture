package cs601.project4.frontendservice;

import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.model.response.GetEventResponseModel;

/**
 * ListEventsHandler is used to list all the events. 
 * 
 * @author kmkhetia
 *
 */ 
public class ListEventHandler extends HttpServlet {
	private static final Logger log = LogManager.getLogger(ListEventHandler.class);
	private EventServiceClient client = new EventServiceClient();
	/**
	 * The method is used to get list of events.
	 * 
	 * @param request
	 * @param response
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try { 
			Gson gson = new Gson();
			LinkedList<GetEventResponseModel> responseBody = client.listEvents();
			if(responseBody != null && !responseBody.isEmpty()) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				PrintWriter out = response.getWriter();
				out.println(gson.toJson(responseBody));
			} 
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			log.error(e);
		}
	}
}
