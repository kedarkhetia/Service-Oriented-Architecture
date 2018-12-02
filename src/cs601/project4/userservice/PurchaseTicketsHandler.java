package cs601.project4.userservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.request.PurchaseTicketsUserModel;

public class PurchaseTicketsHandler extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {	
			int userId = (int) request.getAttribute("userid");
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = readBody(in);
			PurchaseTicketsUserModel body = gson.fromJson(bodyText, PurchaseTicketsUserModel.class);
			ResultSet resultSet = DBManager.getInstance().selectTickets(body.getEventid(), userId);
			response.setStatus(setResponse(resultSet, body, userId));
		} catch (SQLException e) {
			// TODO Do some logging
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return HttpServletResponse.SC_BAD_REQUEST;
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
