package cs601.project4.eventservice;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.helper.HelperClass;
import cs601.project4.model.EventConfig;
import cs601.project4.model.response.CreateUserResponseModel;
import cs601.project4.model.response.PingUserResponseModel;
import cs601.project4.model.request.CreateUserModel;
import cs601.project4.model.request.PurchaseTicketsEventModel;
import cs601.project4.model.request.PurchaseTicketsUserModel;

/**
 * This method is used to communicate with UserService.
 * 
 * @author kmkhetia
 *
 */
public class UserServiceClient {
	private final static Logger log = LogManager.getLogger(UserServiceClient.class);
	
	private EventConfig config;
	
	public UserServiceClient() {
		config = EventConfig.getInstance();
	}
	
	/**
	 * This method pings to user service to check
	 * if user exists or not.
	 * 
	 * @param userid
	 * @return
	 */
	public boolean checkUser(int userid) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL("http://" + config.getUserHost() + ":" + config.getUserPort() + "/ping/" + userid)).openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			String response = HelperClass.validateResponse(connection);
			Gson gson = new Gson();
			PingUserResponseModel status = gson.fromJson(response, PingUserResponseModel.class);
			return status.getStatus();
		} catch (IOException e) {
			log.error(e);
			return false;
		}
	}
	
	/**
	 * This method request user service to update
	 * the tickets table and assign the ticket to
	 * a user. It requests user service on
	 * Request: POST /{userid}/tickets/add
	 * 
	 * @param purchaseTicket
	 * @return
	 */
	public boolean PurchaseTickets(PurchaseTicketsEventModel purchaseTicket) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL("http://" + config.getUserHost() + ":" + config.getUserPort() + "/" + purchaseTicket.getUserid() + "/tickets/add")).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			PurchaseTicketsUserModel purchaseTicketUser = new PurchaseTicketsUserModel();
			purchaseTicketUser.setEventid(purchaseTicket.getEventid());
			purchaseTicketUser.setTickets(purchaseTicket.getTickets());
			Gson gson = new Gson();
			out.write(gson.toJson(purchaseTicketUser).getBytes());
			connection.connect();
			int responseCode = connection.getResponseCode();
			if(responseCode == HttpServletResponse.SC_OK) {
				return true;
			}
			else {
				return false;
			}
		} catch (IOException e) {
			log.error(e);
			return false;
		}
	}
}
