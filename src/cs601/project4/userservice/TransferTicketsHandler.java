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
import cs601.project4.model.request.TransferTicketModel;

/**
 * This is used to transfer tickets from 
 * one user to another user.
 * 
 * @author kmkhetia
 *
 */
public class TransferTicketsHandler extends HttpServlet {
	private final static Logger log = LogManager.getLogger(TransferTicketsHandler.class);
	/**
	 * This method will receive the request and
	 * pass it to transferTickets method to
	 * transfer the tickets.
	 * 
	 * @param request
	 * @param response
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int userId = (int) request.getAttribute("userid");
		BufferedReader in = request.getReader();
		Gson gson = new Gson();
		String bodyText = HelperClass.readBody(in);
		TransferTicketModel body = gson.fromJson(bodyText, TransferTicketModel.class);
		if(!body.isValid()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		else {
			response.setStatus(transferTickets(body, userId));
		}
	}
	/**
	 * This method will help above method to
	 * transfers tickets from userId to target user.
	 * 
	 * @param body
	 * @param userId
	 * @return
	 */
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
			log.error(e);
			return HttpServletResponse.SC_BAD_REQUEST;
		}
	}
	/**
	 * This method adds tickets to new user.
	 * 
	 * @param body
	 * @param userId
	 * @return
	 */
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
			log.error(e);
			return false;
		}
	}
}
