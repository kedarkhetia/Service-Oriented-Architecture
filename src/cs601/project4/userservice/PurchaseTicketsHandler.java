package cs601.project4.userservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.eventservice.UserServiceClient;
import cs601.project4.helper.HelperClass;
import cs601.project4.model.request.PurchaseTicketsUserModel;

/**
 * This is used to purchase tickets for user service.
 * 
 * @author kmkhetia
 *
 */
public class PurchaseTicketsHandler extends HttpServlet {
	private final static Logger log = LogManager.getLogger(PurchaseTicketsHandler.class);
	
	/**
	 * The method is used to purchase tickets.
	 * 
	 * @param request
	 * @param response
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {	
			int userId = (int) request.getAttribute("userid");
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = HelperClass.readBody(in);
			PurchaseTicketsUserModel body = gson.fromJson(bodyText, PurchaseTicketsUserModel.class);
			ResultSet resultSet = DBManager.getInstance().selectTickets(body.getEventid(), userId);
			response.setStatus(setResponse(resultSet, body, userId));
		} catch (SQLException e) {
			log.error(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**
	 * This method is helper method for above method.
	 * 
	 * @param resultSet
	 * @param body
	 * @param userId
	 * @return
	 */
	public int setResponse(ResultSet resultSet, PurchaseTicketsUserModel body, int userId) {
		try {
			boolean res;
			if(resultSet.next()) {
				body.setTickets(resultSet.getInt("TICKETS") + body.getTickets());
				res = DBManager.getInstance().updateTickets(body.getEventid(), userId, body.getTickets());
			}
			else {
				res = DBManager.getInstance().insertTickets(body.getEventid(), userId, body.getTickets());
			}
			return res ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST;
		} catch (SQLException e) {
			log.error(e);
			return HttpServletResponse.SC_BAD_REQUEST;
		}
	}
}
