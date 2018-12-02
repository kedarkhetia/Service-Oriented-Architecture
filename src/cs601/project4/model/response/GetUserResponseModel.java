package cs601.project4.model.response;

import java.util.LinkedList;

public class GetUserResponseModel {
	private int userid;
	private String username;
	private LinkedList<GetEventResponseModel> tickets = new LinkedList<>();
	
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
	public LinkedList<GetEventResponseModel> getTickets() {
		return tickets;
	}
	public void setTickets(LinkedList<GetEventResponseModel> tickets) {
		this.tickets = tickets;
	}
}
