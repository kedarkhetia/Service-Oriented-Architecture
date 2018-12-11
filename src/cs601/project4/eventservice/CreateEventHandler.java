package cs601.project4.eventservice;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.helper.HelperClass;
import cs601.project4.model.request.CreateEventModel;
import cs601.project4.model.response.CreateEventResponseModel;

public class CreateEventHandler extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserServiceClient userClient = new UserServiceClient();
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = HelperClass.readBody(in);
			CreateEventModel body = gson.fromJson(bodyText, CreateEventModel.class);
			if(body != null && body.isValid() && userClient.checkUser(body.getUserid())) {
				int eventId = DBManager.getInstance().insert(body.getEventname(), body.getUserid(), body.getNumtickets());
				if(eventId > 0) {
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
		} catch (Exception e) {
			// TODO log something here.
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} 
	}
}
