package cs601.project4.frontendservice;

import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.response.GetEventResponseModel;

public class ListEventHandler extends HttpServlet {
	private EventServiceClient client = new EventServiceClient();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try { 
			Gson gson = new Gson();
			LinkedList<GetEventResponseModel> responseBody = client.listEvents();
			if(responseBody != null) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				PrintWriter out = response.getWriter();
				out.println(gson.toJson(responseBody));
			} 
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
}
