package cs601.project4.userservice;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.userservice.DBManager;
import cs601.project4.helper.HelperClass;
import cs601.project4.model.request.CreateUserModel;
import cs601.project4.model.response.CreateUserResponseModel;

/**
 * CreateUserHandler is used to create new Users. 
 * 
 * @author kmkhetia
 *
 */ 
public class CreateUserHandler extends HttpServlet {
	private final static Logger log = LogManager.getLogger(CreateUserHandler.class);
	
	/**
	 * The method is used to create new Users.
	 * It is executed when request is received on,
	 * Request: POST /create
	 * 
	 * @param request
	 * @param response
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader in = request.getReader();
			Gson gson = new Gson();
			String bodyText = HelperClass.readBody(in);
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
			log.error(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
