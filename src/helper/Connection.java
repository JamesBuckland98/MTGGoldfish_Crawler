package helper;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
	
	private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());
	
	public static boolean checkInternetConnection() throws InterruptedException {
		int attemps = 0;
		boolean result = false; 
		LOGGER.log(Level.INFO, "Attempting to connect to https://www.mtggoldfish.com/");
		while(attemps < 5 && !result) {
			try {
				URL url = new URL("https://www.mtggoldfish.com/");
				URLConnection connection = url.openConnection();
				connection.connect();
				result = true;
			} catch(Exception e) {
				LOGGER.log(Level.INFO, "No internet connection trying again...");
				attemps++;
			} finally {
				TimeUnit.SECONDS.sleep(5);
			}
		}
		return result;
	}
}
