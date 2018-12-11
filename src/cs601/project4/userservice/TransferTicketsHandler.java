package cs601.project4.userservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.request.TransferTicketModel;

public class TransferTicketsHandler extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int userId = (int) request.getAttribute("userid");
		BufferedReader in = request.getReader();
		Gson gson = new Gson();
		String bodyText = readBody(in);
		TransferTicketModel body = gson.fromJson(bodyText, TransferTicketModel.class);
		if(!body.isValid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		else {
			response.setStatus(transferTickets(body, userId));
		}
	}
	
	public int transferTickets(TransferTicketModel body, int userId) {
		try {
			boolean res = false;
			ResultSet resultSet = DBManager.getInstance().selectTickets(body.getEventid(), userId);
			ResultSet targetUserSet = DBManager.getInstance().select(body.getTargetuser());
			if(resultSet.next() && targetUserSet.next()) {
				int tickets = resultSet.getInt("TICKETS");
				if(tickets >= body.getTickets()) {
					res = DBManager.getInstance().updateTickets(body.getEventid(), userId, tickets - body.getTickets());
					if(res) {
						res = res & addTickets(body, body.getTargetuser());
						if(res) {
							return HttpServletResponse.SC_OK;
						}
						else {
							DBManager.getInstance().updateTickets(body.getEventid(), userId, tickets);
							return HttpServletResponse.SC_BAD_REQUEST;
						}
					}
				}
			}
			return HttpServletResponse.SC_BAD_REQUEST;
		} catch (SQLException e) {
			// TODO Log something here
			e.printStackTrace();
			return HttpServletResponse.SC_BAD_REQUEST;
		}
	}
	
	public boolean addTickets(TransferTicketModel body, int userId) {
		try {
			boolean res;
			ResultSet resultSet = DBManager.getInstance().selectTickets(body.getEventid(), userId);
			if(resultSet.next()) {
				body.setTickets(resultSet.getInt("TICKETS") + body.getTickets());
				res = DBManager.getInstance().updateTickets(body.getEventid(), userId, body.getTickets());
			}
			else {
				res = DBManager.getInstance().insertTickets(body.getEventid(), userId, body.getTickets());
			}
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
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
