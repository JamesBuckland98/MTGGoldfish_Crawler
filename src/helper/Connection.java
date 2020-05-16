package helper;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class Connection {
	
	public static boolean checkInternetConnection() throws InterruptedException {
		int attemps = 0;
		boolean result = false; 
		System.out.println("Attempting to connect to https://www.mtggoldfish.com/");
		while(attemps < 5 && result != true) {
			try {
				URL url = new URL("https://www.mtggoldfish.com/");
				URLConnection connection = url.openConnection();
				connection.connect();
				result = true;
			} catch(Exception e) {
				System.out.println("No internet connection trying again...");
				attemps++;
			} finally {
				TimeUnit.SECONDS.sleep(5);
			}
		}
		return result;
	}
}
