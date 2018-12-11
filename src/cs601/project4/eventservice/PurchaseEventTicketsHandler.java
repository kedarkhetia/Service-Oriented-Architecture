package cs601.project4.eventservice;

import java.io.BufferedReader;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.helper.HelperClass;
import cs601.project4.model.request.PurchaseTicketsEventModel;

/**
 * PurchaseEventTicketsHandler is used to purchase 
 * tickets for event.
 * 
 * @author kmkhetia
 *
 */ 
public class PurchaseEventTicketsHandler extends HttpServlet {
	private final static Logger log = LogManager.getLogger(PurchaseEventTicketsHandler.class);
	
	/**
	 * The method is used to purchase tickets.
	 * Request: POST /purchase/{eventid} 
	 * 
	 * @param request
	 * @param response
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			log.info("Request: " + request.getRequestURL().toString());
			String[] pathParam = request.getPathInfo().split("/");
			int eventId;
			if(pathParam.length > 1 && (eventId = Integer.parseInt(pathParam[1])) > 0) {
				UserServiceClient userClient = new UserServiceClient();
				BufferedReader in = request.getReader();
				Gson gson = new Gson();
				String bodyText = HelperClass.readBody(in);
				PurchaseTicketsEventModel body = gson.fromJson(bodyText, PurchaseTicketsEventModel.class);
				if(body != null && body.isValid() && userClient.checkUser(body.getUserid()) && body.getEventid() == eventId) {
					processTickets(userClient, body, response);
				}
				else {
					log.debug("Couldn't process the request as body might be invalid or user does not exist");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			else {
				log.debug("Invalid eventid or not event id provided as path param.");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}  catch (Exception e) {
			log.error(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} 
	}
	
	/**
	 * This method is helper method for above method.
	 * 
	 * @param userClient
	 * @param body
	 * @param response
	 */
	private void processTickets(UserServiceClient userClient, PurchaseTicketsEventModel body, HttpServletResponse response) {
		try {
			ResultSet resultSet = DBManager.getInstance().select(body.getEventid());
			int avail, purchased;
			if(resultSet.next()) {
				avail = resultSet.getInt("AVAIL");
				purchased = resultSet.getInt("PURCHASED");
				if(avail >= body.getTickets()) {
					DBManager.getInstance().update(body.getEventid(), avail - body.getTickets() , purchased + body.getTickets());
					userClient.PurchaseTickets(body);
					response.setStatus(HttpServletResponse.SC_OK);
				}
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} 
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
