package cs601.project4.model.response;

public class PingUserResponseModel {
	boolean status;
	
	public PingUserResponseModel(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean userid) {
		this.status = status;
	}
	
}