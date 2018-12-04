package cs601.project4.userservice;

public class DBQueryStatements {
	public static final String INSERT = "INSERT INTO USERS (USERNAME) VALUES (?)";
	public static final String SELECT = "SELECT * FROM USERS WHERE USERID=?";
	public static final String SELECT_EVENTS = "SELECT EVENTID, TICKETS FROM TICKETS WHERE USERID=?";
	public static final String SELECT_TICKETS = "SELECT TICKETS FROM TICKETS WHERE EVENTID=? AND USERID=?";
	public static final String UPDATE_TICKETS = "UPDATE TICKETS SET TICKETS=? WHERE EVENTID=? AND USERID=?";
	public static final String PURCHASE_TICKETS = "INSERT INTO TICKETS (TICKETS, EVENTID, USERID) VALUES (?, ?, ?)";
}
