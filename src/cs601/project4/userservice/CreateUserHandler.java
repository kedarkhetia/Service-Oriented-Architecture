package cs601.project4.userservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.userservice.DBManager;
import cs601.project4.model.request.CreateUserModel;
import cs601.project4.model.response.CreateUserResponseModel;

public class CreateUserHandler extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = readBody(in);
			CreateUserModel body = gson.fromJson(bodyText, CreateUserModel.class);
			if(body != null && body.isValid()) {
				int userId = DBManager.getInstance().insert(body.getUsername());
				if(userId != 0) {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_OK);
					CreateUserResponseModel res = new CreateUserResponseModel();
					res.setUserid(userId);
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
