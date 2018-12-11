package cs601.project4.frontendservice;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cs601.project4.eventservice.UserServiceClient;
import cs601.project4.helper.HelperClass;
import cs601.project4.model.FrontEndConfig;
import cs601.project4.model.request.CreateEventModel;
import cs601.project4.model.request.PurchaseTicketsEventModel;
import cs601.project4.model.request.PurchaseTicketsModel;
import cs601.project4.model.response.CreateEventResponseModel;
import cs601.project4.model.response.GetEventResponseModel;

/**
 * It is the client implementation for EventService.
 * 
 * @author kmkhetia
 *
 */
public class EventServiceClient {
	private static final Logger log = LogManager.getLogger(EventServiceClient.class);
	private FrontEndConfig config;
	private String URL;
	public EventServiceClient() {
		config = FrontEndConfig.getInstance();
		URL = "http://" + config.getEventHost() + ":" + config.getEventPort();
	}
	
	/**
	 * It will call createEvent API.
	 * 
	 * @param request
	 * @return
	 */
	public CreateEventResponseModel createEvent(CreateEventModel request) {
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
			return gson.fromJson(response, CreateEventResponseModel.class);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * It will call purchase tickets API.
	 * 
	 * @param request
	 * @param eventid
	 * @param userid
	 * @return
	 */
	public boolean purchaseTickets(PurchaseTicketsModel request, int eventid, int userid) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/purchase/" + eventid)).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			PurchaseTicketsEventModel purchaseTicket = new PurchaseTicketsEventModel();
			purchaseTicket.setEventid(eventid);
			purchaseTicket.setUserid(userid);
			purchaseTicket.setTickets(request.getTickets());
			Gson gson = new Gson();
			out.write(gson.toJson(purchaseTicket).getBytes());
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
	 * This method will call GetEvent API.
	 * 
	 * @param eventId
	 * @return
	 */
	public GetEventResponseModel getEvent(int eventId) {
		try {	
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/" + eventId)).openConnection();
			connection.setRequestMethod("GET");
			Gson gson = new Gson();
			connection.connect();
			if(connection.getResponseCode() == HttpServletResponse.SC_BAD_REQUEST) {
				return null;
			}
			String response = HelperClass.validateResponse(connection);
			return gson.fromJson(response, GetEventResponseModel.class);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * It will call GetList API.
	 * 
	 * @return
	 */
	public LinkedList<GetEventResponseModel> listEvents() {
		try {	
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/list")).openConnection();
			connection.setRequestMethod("GET");
			Gson gson = new Gson();
			connection.connect();
			if(connection.getResponseCode() == HttpServletResponse.SC_BAD_REQUEST) {
				return null;
			}
			String response = HelperClass.validateResponse(connection);
			Type listType = new TypeToken<LinkedList<GetEventResponseModel>>(){}.getType();
			return gson.fromJson(response, listType);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
}
