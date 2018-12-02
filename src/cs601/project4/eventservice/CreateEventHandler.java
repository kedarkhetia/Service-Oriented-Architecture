package cs601.project4.eventservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.request.CreateEventModel;
import cs601.project4.model.response.CreateEventResponseModel;

public class CreateEventHandler extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = readBody(in);
			CreateEventModel body = gson.fromJson(bodyText, CreateEventModel.class);
			if(body != null && body.isValid()) {
				int eventId = DBManager.getInstance().insert(body.getEventname(), body.getUserid(), body.getNumtickets());
				if(eventId != 0) {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_OK);
					CreateEventResponseModel res = new CreateEventResponseModel();
					res.setEventid(eventId);
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
