package cs601.project4.model.response;

/**
 * EventId Model to store EventIds for User Service.
 * 
 * @author kmkhetia
 *
 */
public class EventIdModel {
	int eventid;
	
	public EventIdModel(int eventid) {
		this.eventid = eventid;
	}
	
	public int getEventid() {
		return eventid;
	}

	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
	
}
