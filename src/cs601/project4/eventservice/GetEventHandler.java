package cs601.project4.eventservice;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import cs601.project4.model.response.GetEventResponseModel;

/**
 * GetEventHandler is used to get the event.
 * 
 * @author kmkhetia
 *
 */
public class GetEventHandler extends HttpServlet {
	private final static Logger log = LogManager.getLogger(GetEventHandler.class);
	/**
	 * The method is used to get Events.
	 * It is executed when request is received on,
	 * Request: Get /{eventid}
	 * 
	 * @param request
	 * @param response
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			log.info("Request: " + request.getRequestURL().toString());
			String[] pathParam = request.getPathInfo().split("/");
			int eventId;
			if(pathParam.length > 1 && (eventId = Integer.parseInt(pathParam[1])) > 0) {
				Gson gson = new Gson();
				ResultSet result = DBManager.getInstance().select(eventId);
				if(result.next()) {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_OK);
					GetEventResponseModel res = new GetEventResponseModel();
					setData(res, result);
					PrintWriter out = response.getWriter();
					out.println(gson.toJson(res));
				}
				else {
					log.debug("Empty result set for Select eventId");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			else {
				log.debug("Invalid EventId or EventId not present");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error(e);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	private void setData(GetEventResponseModel res, ResultSet result) throws SQLException {
		res.setAvail(result.getInt("AVAIL"));
		res.setEventid(result.getInt("EVENTID"));
		res.setEventname(result.getString("EVENTNAME"));
		res.setPurchased(result.getInt("PURCHASED"));
		res.setUserid(result.getInt("CREATEDBY"));
	}
}
