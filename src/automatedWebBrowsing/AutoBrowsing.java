package automatedWebBrowsing;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import helper.Connection;
import helper.Read;
import helper.Write;
import setUp.Config;

public class AutoBrowsing {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ArrayList<String> cardList = Read.readTxtFile(args[0]);
		ArrayList<String> blacklist = Read.readTxtFile("blacklist.txt");
		
		WebDriver driver = Config.setUp();
		
		if(Connection.checkInternetConnection()) {
			for(String cardName: cardList) {
					try {
						Write.writeToCsv(Crawler.searchForCard(cardName, blacklist, driver), args[1]);
					} catch(Exception e) {
						System.out.println("error " + e);
					} finally {
						System.out.println(cardName + " search complete!");
					}
				}
		}
			
		System.out.println("All cards searched!");
	
		//closing the browser
		driver.quit(); 
	}
}
