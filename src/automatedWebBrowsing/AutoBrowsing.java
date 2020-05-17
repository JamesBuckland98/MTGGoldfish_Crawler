package automatedWebBrowsing;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openqa.selenium.WebDriver;

import helper.Connection;
import helper.Read;
import helper.Write;
import setUp.Config;

public class AutoBrowsing {
	
	private static final Logger LOGGER = Logger.getLogger(AutoBrowsing.class.getName());
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		WebDriver driver = null;
		
		Options options = new Options();
		Option headlessMode = new Option("b", false, "Run in background mode");
		options.addOption(headlessMode);

		List<String> cardList = Read.readTxtFile(args[0]);
		List<String> blacklist = Read.readTxtFile("blacklist.txt");
		
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        
        try {
        	CommandLine cmd = parser.parse(options, args);
        	if(cmd.hasOption("b")) {
        		driver = Config.setUp("headless");
        	} else {
        		driver = Config.setUp();
        	}
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
		
		
		
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
