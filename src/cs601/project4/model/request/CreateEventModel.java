package cs601.project4.model.request;

public class CreateEventModel {
	private int userid;
	private String eventname;
	private int numtickets;
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public int getNumtickets() {
		return numtickets;
	}
	public void setNumtickets(int numtickets) {
		this.numtickets = numtickets;
	}
	
	public boolean isValid() {
		if(eventname == null || userid == 0 || numtickets == 0) {
			return false;
		}
		return true;
	}
}
