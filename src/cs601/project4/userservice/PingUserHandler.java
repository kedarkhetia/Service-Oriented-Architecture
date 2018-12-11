package cs601.project4.userservice;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.eventservice.UserServiceClient;
import cs601.project4.model.response.PingUserResponseModel;

/**
 * This is used to check if user exists or not.
 * 
 * @author kmkhetia
 *
 */
public class PingUserHandler extends HttpServlet {
	private final static Logger log = LogManager.getLogger(PingUserHandler.class);
	
	/**
	 * This is used to check if user exists or not. 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pathParam = request.getPathInfo().split("/");
			int userId;
			if(pathParam.length > 1 && (userId = Integer.parseInt(pathParam[1])) > 0) {
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
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			log.error(e);
		}
	}
}
