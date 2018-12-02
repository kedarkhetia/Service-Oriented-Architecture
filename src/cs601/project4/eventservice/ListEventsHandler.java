package cs601.project4.eventservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cs601.project4.model.response.GetEventResponseModel;

public class ListEventsHandler extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			Gson gson = new Gson();
			ResultSet result = DBManager.getInstance().selectAll();
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			LinkedList<GetEventResponseModel> responseList = new LinkedList<>();
			while(result.next()) {
				GetEventResponseModel res = new GetEventResponseModel();
				setData(res, result);
				responseList.add(res);
			}
			Type listType = new TypeToken<LinkedList<GetEventResponseModel>>(){}.getType();
			PrintWriter out = response.getWriter();
			out.println(gson.toJson(responseList, listType));
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
