package cs601.project4.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
/**
 * Helper Class that is used to read data from input stream
 * 
 * @author kmkhetia
 *
 */
public class HelperClass {
	/**
	 * This method will read the body of the request.
	 * 
	 * @param in
	 * @return
	 */
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
	
	/**
	 * This method will validate the response that is received
	 * upon querying the client.
	 * 
	 * @param connection
	 * @return
	 */
	public static String validateResponse(HttpURLConnection connection) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				return readBody(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}
}
