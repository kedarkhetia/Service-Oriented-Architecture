package cs601.project4.frontendservice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.helper.HelperClass;
import cs601.project4.model.request.PurchaseTicketsModel;
import cs601.project4.model.response.GetEventResponseModel;

public class GetEventHandler extends HttpServlet {
	private EventServiceClient client = new EventServiceClient();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int eventId;
			if(pathParam.length > 1 && (eventId = Integer.parseInt(pathParam[1])) > 0) {
				Gson gson = new Gson();
				GetEventResponseModel responseBody = client.getEvent(eventId);
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
		} catch (IOException e) {
			// TODO log something here.
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int eventId;
			int userId;
			Gson gson = new Gson();
			if(pathParam.length > 3 && (eventId = Integer.parseInt(pathParam[1])) > 0 && (userId = Integer.parseInt(pathParam[3])) > 0 && pathParam[2].equals("purchase")) {
				String bodyText = HelperClass.readBody(request.getReader());
				PurchaseTicketsModel body = gson.fromJson(bodyText, PurchaseTicketsModel.class);
				if(body != null && body.isValid()) {
					boolean res = client.purchaseTickets(body, eventId, userId);
					if(res) {
						response.setStatus(HttpServletResponse.SC_OK);
					}
					else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				}
			}
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (IOException e) {
			// TODO log something here.
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
