package cs601.project4.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
/**
 * It represents config file for FrontEnd service.
 * 
 * @author kmkhetia
 *
 */
public class FrontEndConfig {
	private int appPort;
	private int dbPort;
	private String dbName;
	private String dbHost;
	private String username;
	private String password;
	private String userHost;
	private int userPort;
	private String eventHost;
	private int eventPort;
	
	private static String CONFIG_PATH = "FrontEndConfig.json";
	private static FrontEndConfig config;
	private FrontEndConfig() {}
	
	public static FrontEndConfig getInstance() {
		if(config == null) {
			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			try {
				BufferedReader in = Files.newBufferedReader(Paths.get(CONFIG_PATH));
				String line;
				while((line = in.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			config = gson.fromJson(sb.toString(), FrontEndConfig.class);
		}
		return config;
	}
	
	public String getEventHost() {
		return eventHost;
	}
	public void setEventHost(String eventHost) {
		this.eventHost = eventHost;
	}
	public int getEventPort() {
		return eventPort;
	}
	public void setEventPort(int eventPort) {
		this.eventPort = eventPort;
	}
	public String getUserHost() {
		return userHost;
	}
	public void setUserHost(String userHost) {
		this.userHost = userHost;
	}
	public int getUserPort() {
		return userPort;
	}
	public void setUserPort(int userPort) {
		this.userPort = userPort;
	}
	public int getAppPort() {
		return appPort;
	}
	public void setAppPort(int appPort) {
		this.appPort = appPort;
	}
	public int getDbPort() {
		return dbPort;
	}
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbHost() {
		return dbHost;
	}
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
