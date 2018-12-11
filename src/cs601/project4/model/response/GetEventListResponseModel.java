package cs601.project4.model.response;

import java.util.LinkedList;

/**
 * Response object for GetEventList API.
 * 
 * @author kmkhetia
 *
 */
public class GetEventListResponseModel {
	private LinkedList<GetEventResponseModel> list = new LinkedList<>();

	public LinkedList<GetEventResponseModel> getList() {
		return list;
	}

	public void setList(LinkedList<GetEventResponseModel> list) {
		this.list = list;
	}
	
}
