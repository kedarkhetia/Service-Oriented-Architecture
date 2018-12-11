package cs601.project4.model.response;

import java.util.LinkedList;

public class GetUserResponseUserModel {
	private int userid;
	private String username;
	private LinkedList<EventIdModel> tickets = new LinkedList<EventIdModel>();
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LinkedList<EventIdModel> getTickets() {
		return tickets;
	}
	public void setTickets(LinkedList<EventIdModel> tickets) {
		this.tickets = tickets;
	}
	
	public boolean isValid() {
		if(username == null || username.isEmpty() || userid <= 0) {
			return false;
		}
		return true;
	}
}
