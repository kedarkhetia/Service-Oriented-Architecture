package cs601.project4.eventservice;

public class DBQueryStatements {
	public static final String INSERT = "INSERT INTO EVENTS (EVENTNAME, CREATEDBY, AVAIL, PURCHASED) VALUES (?, ?, ?, 0)";
	public static final String SELECT = "SELECT * FROM EVENTS WHERE EVENTID=?";
	public static final String UPDATE = "UPDATE EVENTS SET PURCHASED=?, AVAIL=? WHERE EVENTID=?";
	public static final String SELECT_ALL = "SELECT * FROM EVENTS";
}
