package cs601.project4.userservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs601.project4.model.UserConfig;

public class DBManager {
	private static DBManager dbm;
	private Connection con;
	
	private DBManager() throws SQLException {
		UserConfig config = UserConfig.getInstance();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String urlString = "jdbc:mysql://" + config.getDbHost() + 
				":" + config.getDbPort() + 
				"/" + config.getDbName();
		String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		this.con = DriverManager.getConnection(urlString+timeZoneSettings, config.getUsername(), config.getPassword());
	}
	
	public synchronized static DBManager getInstance() throws SQLException {
		if(dbm == null) {
			dbm = new DBManager();
		}
		return dbm;
	}
	
	public synchronized int insert(String userName) {
		try {
			PreparedStatement insert = con.prepareStatement(DBQueryStatements.INSERT, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, userName);
			int val = insert.executeUpdate();
			if(val == 0) {
				//throw new SQLException("Creating user failed, no rows affected.");
				return 0;
			}
			try(ResultSet keys = insert.getGeneratedKeys()) {
				if (keys.next()) {
	                return keys.getInt(1);
	            }
	            else {
	                //throw new SQLException("Creating user failed, no ID obtained.");
	            	return 0;
	            }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public synchronized ResultSet select(int userId) {
		try {
			PreparedStatement select = con.prepareStatement(DBQueryStatements.SELECT);
			select.setInt(1, userId);
			return select.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized ResultSet selectEvents(int userId) {
		try {
			PreparedStatement selectEvents = con.prepareStatement(DBQueryStatements.SELECT_EVENTS);
			selectEvents.setInt(1, userId);
			return selectEvents.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized ResultSet selectTickets(int eventId, int userId) {
		try{
			PreparedStatement selectTickets = con.prepareStatement(DBQueryStatements.SELECT_TICKETS);
			selectTickets.setInt(1, eventId);
			selectTickets.setInt(2, userId);
			return selectTickets.executeQuery();
		} catch (SQLException e) {
			// TODO Log something here
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized boolean insertTickets(int eventId, int userId, int tickets) {
		if(tickets < 0) {
			return false;
		}
		try{
			PreparedStatement insertTickets = con.prepareStatement(DBQueryStatements.PURCHASE_TICKETS);
			insertTickets.setInt(1, tickets);
			insertTickets.setInt(2, eventId);
			insertTickets.setInt(3, userId);
			int val = insertTickets.executeUpdate();
			if(val == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			// TODO Log something here
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized boolean updateTickets(int eventId, int userId, int tickets) {
		if(tickets < 0) {
			return false;
		}
		try {
			PreparedStatement update = con.prepareStatement(DBQueryStatements.UPDATE_TICKETS);
			update.setInt(1, tickets);
			update.setInt(2, eventId);
			update.setInt(3, userId);
			int val = update.executeUpdate();
			if(val == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
