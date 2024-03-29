package cs601.project4.eventservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project4.model.EventConfig;

/**
 * This is DBManager for EventService. It is used
 * to execute DB based on the request received. It
 * abstracts query execution and preparation from 
 * application logic.
 * 
 * @author kmkhetia
 *
 */ 
public class DBManager {
	private static DBManager dbm;
	private Connection con;
	
	private final static Logger log = LogManager.getLogger(DBManager.class);
	
	private DBManager() throws SQLException {
		EventConfig config = EventConfig.getInstance();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error(e);
		}
		String urlString = "jdbc:mysql://" + config.getDbHost() + 
				":" + config.getDbPort() + 
				"/" + config.getDbName();
		String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true";
		this.con = DriverManager.getConnection(urlString+timeZoneSettings, config.getUsername(), config.getPassword());
		log.info("Connected to Database with URL: " + urlString);
	}
	
	/**
	 *	The method is executed when Instance of DBManager is required.
	 */
	public synchronized static DBManager getInstance() {
		try {
			if(dbm == null) {
				dbm = new DBManager();
			}
			return dbm;
		} catch (SQLException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * This is used to insert new events to DB.
	 * It returns new eventId generated by Database.
	 * 
	 * @param eventName
	 * @param createdBy
	 * @param avail
	 * @return
	 */
	public synchronized int insert(String eventName, int createdBy, int avail) {
		try {
			PreparedStatement insert = con.prepareStatement(DBQueryStatements.INSERT, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, eventName);
			insert.setInt(2, createdBy);
			insert.setInt(3, avail);
			int val = insert.executeUpdate();
			if(val == 0) {
				log.debug("Creating event failed, no rows affected");
				return 0;
			}
			try(ResultSet keys = insert.getGeneratedKeys()) {
				if (keys.next()) {
	                return keys.getInt(1);
	            }
	            else {
	            	log.debug("Creating event failed, no ID obtained.");
	            	return 0;
	            }
			}
		} catch (SQLException e) {
			log.error(e);
			return 0;
		}
	}
	
	/**
	 * This method executes select query on DB.
	 * It is used to get Event information
	 * based on eventId.
	 * 
	 * @param eventId
	 * @return
	 */
	public synchronized ResultSet select(int eventId) {
		try {
			PreparedStatement select = con.prepareStatement(DBQueryStatements.SELECT);
			select.setInt(1, eventId);
			return select.executeQuery();
		} catch (SQLException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * This method executes select query on DB
	 * without any conditional clause.
	 * 
	 * @return
	 */
	public synchronized ResultSet selectAll() {
		try {
			PreparedStatement select = con.prepareStatement(DBQueryStatements.SELECT_ALL);
			return select.executeQuery();
		} catch (SQLException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * This method executes update query on DB.
	 * It updates available and purchased number
	 * of tickets for provided eventId.
	 * 
	 * @param eventId
	 * @param avail
	 * @param purchased
	 * @return
	 */
	public synchronized boolean update(int eventId, int avail, int purchased) {
		if(avail < 0 || purchased < 0) {
			log.debug("Available or purchase less than 0, avail: " + avail + " purchased: " + purchased);
			return false;
		}
		try {
			PreparedStatement update = con.prepareStatement(DBQueryStatements.UPDATE);
			update.setInt(1, avail);
			update.setInt(2, purchased);
			update.setInt(3, eventId);
			int val = update.executeUpdate();
			if(val == 0) {
				log.debug("Update query didn't updated any rows!");
				return false;
			}
			return true;
		} catch (SQLException e) {
			log.error(e);
			return false;
		}
	}
}
