package cs601.project4.frontendservice;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.helper.HelperClass;
import cs601.project4.model.request.TransferTicketModel;
import cs601.project4.model.response.GetUserResponseModel;

public class GetUserHandler extends HttpServlet {
	private UserServiceClient client = new UserServiceClient();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int userId;
			if(pathParam.length > 1 && (userId = Integer.parseInt(pathParam[1])) > 0) {
				Gson gson = new Gson();
				GetUserResponseModel responseBody = client.getUser(new EventServiceClient(), userId);
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
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int userId;
			if(pathParam.length > 3 && (userId = Integer.parseInt(pathParam[1])) > 0 && request.getPathInfo().endsWith("/tickets/transfer")) {
				Gson gson = new Gson();
				TransferTicketModel body = gson.fromJson(HelperClass.readBody(request.getReader()), TransferTicketModel.class);
				if(body != null && body.isValid()) {
					boolean res = client.tranferTickets(body, userId);
					if(res) {
						response.setStatus(HttpServletResponse.SC_OK);
					}
					else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				} 
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
}
