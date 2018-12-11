package cs601.project4.eventservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs601.project4.model.EventConfig;

public class DBManager {
	private static DBManager dbm;
	private Connection con;
	
	private DBManager() throws SQLException {
		EventConfig config = EventConfig.getInstance();
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
	
	public synchronized static DBManager getInstance() {
		try {
			if(dbm == null) {
				dbm = new DBManager();
			}
			return dbm;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized int insert(String eventName, int createdBy, int avail) {
		try {
			PreparedStatement insert = con.prepareStatement(DBQueryStatements.INSERT, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, eventName);
			insert.setInt(2, createdBy);
			insert.setInt(3, avail);
			int val = insert.executeUpdate();
			if(val == 0) {
				//throw new SQLException("Creating event failed, no rows affected.");
				return 0;
			}
			try(ResultSet keys = insert.getGeneratedKeys()) {
				if (keys.next()) {
	                return keys.getInt(1);
	            }
	            else {
	            	return 0;
	                //throw new SQLException("Creating event failed, no ID obtained.");
	            }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public synchronized ResultSet select(int eventId) {
		try {
			PreparedStatement select = con.prepareStatement(DBQueryStatements.SELECT);
			select.setInt(1, eventId);
			return select.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public synchronized ResultSet selectAll() {
		try {
			PreparedStatement select = con.prepareStatement(DBQueryStatements.SELECT_ALL);
			return select.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public synchronized boolean update(int eventId, int avail, int purchased) {
		if(avail < 0 || purchased < 0) {
			return false;
		}
		try {
			PreparedStatement update = con.prepareStatement(DBQueryStatements.UPDATE);
			update.setInt(1, avail);
			update.setInt(2, purchased);
			update.setInt(3, eventId);
			int val = update.executeUpdate();
			if(val == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
