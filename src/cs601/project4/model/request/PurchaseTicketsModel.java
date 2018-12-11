package cs601.project4.model.request;

public class PurchaseTicketsModel {
	private int tickets;

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
	}

	public boolean isValid() {
		if(tickets <= 0) {
			return false;
		}
		return true;
	}
}
