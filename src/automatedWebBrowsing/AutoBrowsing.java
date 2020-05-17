package automatedWebBrowsing;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;

import helper.Connection;
import helper.Read;
import helper.Write;
import setUp.Config;

public class AutoBrowsing {
	
	private static final Logger LOGGER = Logger.getLogger(AutoBrowsing.class.getName());
	
	public static void main(String[] args) throws IOException, InterruptedException {
		List<String> cardList = Read.readTxtFile(args[0]);
		List<String> blacklist = Read.readTxtFile("blacklist.txt");
		
		WebDriver driver = Config.setUp();
		
		if(Connection.checkInternetConnection()) {
			for(String cardName: cardList) {
					try {
						Write.writeToCsv(Crawler.searchForCard(cardName, blacklist, driver), args[1]);
					} catch(Exception e) {
						LOGGER.log(Level.WARNING, e.getMessage());
					} finally {
						String message = String.format("%s search complete", cardName);
						LOGGER.log(Level.INFO, message);
					}
				}
		}

		LOGGER.log(Level.INFO, "All cards searched!");
	
		//closing the browser
		driver.quit(); 
	}
}
