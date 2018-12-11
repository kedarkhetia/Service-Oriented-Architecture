package cs601.project4.frontendservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.FrontEndConfig;
import cs601.project4.model.request.CreateUserModel;
import cs601.project4.model.request.TransferTicketModel;
import cs601.project4.model.response.CreateUserResponseModel;
import cs601.project4.model.response.EventIdModel;
import cs601.project4.model.response.GetEventResponseModel;
import cs601.project4.model.response.GetUserResponseModel;
import cs601.project4.model.response.GetUserResponseUserModel;

public class UserServiceClient {
	private FrontEndConfig config;
	private String URL;
	public UserServiceClient() {
		config = FrontEndConfig.getInstance();
		URL = "http://" + config.getUserHost() + ":" + config.getUserPort();
	}
	
	public GetUserResponseModel getUser(EventServiceClient eventClient, int userid) {
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(URL + "/" + userid)).openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			String responseData = validateResponse(connection);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
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
			String response = validateResponse(connection);
			return gson.fromJson(response, CreateUserResponseModel.class);
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
