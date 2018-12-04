package cs601.project4.userservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cs601.project4.model.response.PingUserResponseModel;

public class PingUserHandler extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int userId;
			if(pathParam.length > 1 && (userId = Integer.parseInt(pathParam[1])) != 0) {
				Gson gson = new Gson();
				ResultSet userData = DBManager.getInstance().select(userId);
				PingUserResponseModel res;
				if(userData.next()) {
					res = new PingUserResponseModel(true);
				} 
				else {
					res = new PingUserResponseModel(false);
				}
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.println(gson.toJson(res));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
}
