package cs601.project4.model.response;

/**
 * Response object for GetEvent API.
 * 
 * @author kmkhetia
 *
 */
public class GetEventResponseModel {
	private int eventid;
	private String eventname;
	private int userid;
	private int avail;
	private int purchased;
	
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getAvail() {
		return avail;
	}
	public void setAvail(int avail) {
		this.avail = avail;
	}
	public int getPurchased() {
		return purchased;
	}
	public void setPurchased(int purchased) {
		this.purchased = purchased;
	}
}
