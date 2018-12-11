package cs601.project4.frontendservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cs601.project4.model.FrontEndConfig;
import cs601.project4.model.request.CreateEventModel;
import cs601.project4.model.request.PurchaseTicketsEventModel;
import cs601.project4.model.request.PurchaseTicketsModel;
import cs601.project4.model.response.CreateEventResponseModel;
import cs601.project4.model.response.GetEventResponseModel;

public class EventServiceClient {
	private FrontEndConfig config;
	private String URL;
	public EventServiceClient() {
		config = FrontEndConfig.getInstance();
		URL = "http://" + config.getEventHost() + ":" + config.getEventPort();
	}
	
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
			String response = validateResponse(connection);
			return gson.fromJson(response, CreateEventResponseModel.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public GetEventResponseModel getEvent(int eventId) {
		try {	
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/" + eventId)).openConnection();
			connection.setRequestMethod("GET");
			Gson gson = new Gson();
			connection.connect();
			if(connection.getResponseCode() == HttpServletResponse.SC_BAD_REQUEST) {
				return null;
			}
			String response = validateResponse(connection);
			return gson.fromJson(response, GetEventResponseModel.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public LinkedList<GetEventResponseModel> listEvents() {
		try {	
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/list")).openConnection();
			connection.setRequestMethod("GET");
			Gson gson = new Gson();
			connection.connect();
			if(connection.getResponseCode() == HttpServletResponse.SC_BAD_REQUEST) {
				return null;
			}
			String response = validateResponse(connection);
			Type listType = new TypeToken<LinkedList<GetEventResponseModel>>(){}.getType();
			return gson.fromJson(response, listType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String validateResponse(HttpURLConnection connection) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		String response = "";
		while((line = reader.readLine()) != null) {
			response += line;
		}
		return response;
	}
}
