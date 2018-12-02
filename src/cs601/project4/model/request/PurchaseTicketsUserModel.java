package cs601.project4.model.request;

public class PurchaseTicketsUserModel {
	private int eventid;
	private int tickets;
	
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	public int getTickets() {
		return tickets;
	}
	public void setTickets(int tickets) {
		this.tickets = tickets;
	}
	
	public boolean isValid() {
		if(eventid == 0 || tickets == 0) {
			return false;
		}
		return true;
	}
}
