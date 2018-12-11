package cs601.project4.model.request;
/**
 * Request object for CreateUser API.
 * 
 * @author kmkhetia
 *
 */
public class CreateUserModel {
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isValid() {
		if(username != null && !username.isEmpty()) {
			return true;
		}
		return false;
	}
}
