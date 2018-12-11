package cs601.project4.frontendservice;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.helper.HelperClass;
import cs601.project4.model.FrontEndConfig;
import cs601.project4.model.request.CreateUserModel;
import cs601.project4.model.request.TransferTicketModel;
import cs601.project4.model.response.CreateUserResponseModel;
import cs601.project4.model.response.EventIdModel;
import cs601.project4.model.response.GetEventResponseModel;
import cs601.project4.model.response.GetUserResponseModel;
import cs601.project4.model.response.GetUserResponseUserModel;

/**
 * It is the client implementation for UserService.
 * 
 * @author kmkhetia
 *
 */
public class UserServiceClient {
	private static final Logger log = LogManager.getLogger(UserServiceClient.class);
	private FrontEndConfig config;
	private String URL;
	public UserServiceClient() {
		config = FrontEndConfig.getInstance();
		URL = "http://" + config.getUserHost() + ":" + config.getUserPort();
	}
	
	/**
	 * It requests getUser API.
	 * 
	 * @param eventClient
	 * @param userid
	 * @return
	 */
	public GetUserResponseModel getUser(EventServiceClient eventClient, int userid) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/" + userid)).openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			String responseData = HelperClass.validateResponse(connection);
			Gson gson = new Gson();
			GetUserResponseUserModel userResponse = gson.fromJson(responseData, GetUserResponseUserModel.class);
			GetUserResponseModel response = new GetUserResponseModel();
			response.setUserid(userResponse.getUserid());
			response.setUsername(userResponse.getUsername());
			LinkedList<GetEventResponseModel> tickets = new LinkedList<>();
			for(EventIdModel i : userResponse.getTickets()) {
				tickets.add(eventClient.getEvent(i.getEventid()));
			}
			response.setTickets(tickets);
			return response;
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * It calls TransferTickets API.
	 * 
	 * @param request
	 * @param userid
	 * @return
	 */
	public boolean tranferTickets(TransferTicketModel request, int userid) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/" + userid + "/tickets/transfer")).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			Gson gson = new Gson();
			out.write(gson.toJson(request).getBytes());
			connection.connect();
			if(connection.getResponseCode() == HttpServletResponse.SC_BAD_REQUEST) {
				return false;
			}
			return true;
		} catch (IOException e) {
			log.error(e);
			return false;
		}
	}
	
	/**
	 * This method calls CreateUser API.
	 * 
	 * @param request
	 * @return
	 */
	public CreateUserResponseModel createUser(CreateUserModel request) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/create")).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			Gson gson = new Gson();
			out.write(gson.toJson(request).getBytes());
			connection.connect();
			if(connection.getResponseCode() == HttpServletResponse.SC_BAD_REQUEST) {
				return null;
			}
			String response = HelperClass.validateResponse(connection);
			return gson.fromJson(response, CreateUserResponseModel.class);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
}
