package cs601.project4.eventservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.response.GetEventResponseModel;

public class GetEventHandler extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int eventId;
			if(pathParam.length > 1 && (eventId = Integer.parseInt(pathParam[1])) > 0) {
				Gson gson = new Gson();
				ResultSet result = DBManager.getInstance().select(eventId);
				if(result.next()) {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_OK);
					GetEventResponseModel res = new GetEventResponseModel();
					setData(res, result);
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
		} catch (IOException e) {
			// TODO log something here.
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void setData(GetEventResponseModel res, ResultSet result) throws SQLException {
		res.setAvail(result.getInt("AVAIL"));
		res.setEventid(result.getInt("EVENTID"));
		res.setEventname(result.getString("EVENTNAME"));
		res.setPurchased(result.getInt("PURCHASED"));
		res.setUserid(result.getInt("CREATEDBY"));
	}
}
