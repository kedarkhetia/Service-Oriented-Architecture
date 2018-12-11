package cs601.project4.model.request;
/**
 * Request object for TransferTicket API.
 * 
 * @author kmkhetia
 *
 */
public class TransferTicketModel {
	private int eventid;
	private int tickets;
	private int targetuser;
	
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
	public int getTargetuser() {
		return targetuser;
	}
	public void setTargetuser(int targetuser) {
		this.targetuser = targetuser;
	}
	public boolean isValid() {
		if(eventid <= 0 || tickets <= 0 || targetuser <= 0) {
			return false;
		}
		return true;
	}
}
