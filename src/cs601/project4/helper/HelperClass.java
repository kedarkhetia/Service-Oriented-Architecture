package cs601.project4.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HelperClass {
	public static String readBody(BufferedReader in) {
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String validateResponse(HttpURLConnection connection) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			String response = "";
			while((line = reader.readLine()) != null) {
				response += line;
			}
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
