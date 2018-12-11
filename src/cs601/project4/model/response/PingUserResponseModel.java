package cs601.project4.model.response;

/**
 * Response object for PingUser API.
 * 
 * @author kmkhetia
 *
 */
public class PingUserResponseModel {
	boolean status;
	
	public PingUserResponseModel(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
