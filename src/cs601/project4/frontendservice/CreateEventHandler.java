package cs601.project4.frontendservice;

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
	private EventServiceClient client = new EventServiceClient();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = HelperClass.readBody(in);
			CreateEventModel body = gson.fromJson(bodyText, CreateEventModel.class);
			if(body != null && body.isValid()) {
				CreateEventResponseModel responseBody = client.createEvent(body);
				if(responseBody != null) {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_OK);
					PrintWriter out = response.getWriter();
					out.println(gson.toJson(responseBody));
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
