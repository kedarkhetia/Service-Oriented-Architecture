package cs601.project4.userservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.response.EventIdModel;
import cs601.project4.model.response.GetUserResponseUserModel;

public class GetUserHandler extends HttpServlet {
	
	private final String PURCHASE = "add";
	private final String TRANSFER = "transfer";
	private final String TICKETS = "tickets";
	private final String PURCHASE_SERVLET = "/tickets/add";
	private final String TRANSFER_SERVLET = "/tickets/transfer";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int userId;
			if(pathParam.length > 1 && (userId = Integer.parseInt(pathParam[1])) != 0) {
				Gson gson = new Gson();
				ResultSet userData = DBManager.getInstance().select(userId);
				ResultSet eventData = DBManager.getInstance().selectEvents(userId);
				GetUserResponseUserModel res = new GetUserResponseUserModel();
				setData(userData, eventData, res);
				if(res.isValid()) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("application/json");
					PrintWriter out = response.getWriter();
					out.println(gson.toJson(res));
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
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String[] pathParam = request.getPathInfo().split("/");
		int userId;
		if(pathParam.length > 3 && (userId = Integer.parseInt(pathParam[1])) != 0 && pathParam[2].equals(TICKETS)) {
			try {
				request.setAttribute("userid", userId);
				if(pathParam[3].equals(PURCHASE)) {
					request.getRequestDispatcher(PURCHASE_SERVLET).forward(request, response); 
				}
				else if(pathParam[3].equals(TRANSFER)) {
					request.getRequestDispatcher(TRANSFER_SERVLET).forward(request, response);
				}
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				e.printStackTrace();
			}
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void setData(ResultSet userData, ResultSet eventData, GetUserResponseUserModel res) throws SQLException {
		if(userData.next()) {
			res.setUserid(userData.getInt("USERID"));
			res.setUsername(userData.getString("USERNAME"));
		}
		LinkedList<EventIdModel> tickets = new LinkedList<EventIdModel>();
		while(eventData.next()) {
			int numberOfTickets = eventData.getInt("TICKETS");
			for(int i=0; i < numberOfTickets; i++) {
				tickets.add(new EventIdModel(eventData.getInt("EVENTID")));
			}
		}
		res.setTickets(tickets);
	}
}
