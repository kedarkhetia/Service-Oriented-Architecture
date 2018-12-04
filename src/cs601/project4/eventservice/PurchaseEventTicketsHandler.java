package cs601.project4.eventservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.request.PurchaseTicketsEventModel;

public class PurchaseEventTicketsHandler extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserServiceClient userClient = new UserServiceClient();
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = readBody(in);
			PurchaseTicketsEventModel body = gson.fromJson(bodyText, PurchaseTicketsEventModel.class);
			if(body != null && body.isValid() && userClient.checkUser(body.getUserid())) {
				processTickets(userClient, body, response);
			}
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}  catch (IOException e) {
			// TODO log something here.
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} 
	}
	
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	public String readBody(BufferedReader in) {
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
