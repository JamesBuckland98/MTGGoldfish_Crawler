package automatedWebBrowsing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import helper.TextCleaner;
import model.Card;

public class Crawler {
	
	private static final Logger LOGGER = Logger.getLogger(Crawler.class.getName());
	
	private static ArrayList<Card> allCards = new ArrayList<>();
	private static String cardName;
	private static WebDriver driver;
	private static Card card = new Card();

	public static List<Card> searchForCard(String cardName, List<String> blacklist, WebDriver driver) {
		Crawler.driver = driver;
		Crawler.cardName = cardName;
		String message = String.format("Seaching for card: %s ...", cardName);
		LOGGER.log(Level.INFO, message);
		driver.get("https://www.mtggoldfish.com/");
		WebElement searchBar = driver.findElement(By.id("query_string"));
		searchBar.sendKeys(cardName);
		searchBar.sendKeys(Keys.ENTER);
		findTable(blacklist);
		return allCards;
	}
	
	private static void findTable(List<String> blacklist) {
		try {
			WebElement table = driver.findElement(By.className("table-striped"));
			List<WebElement> tableRow = table.findElements(By.tagName("tr"));
			for(WebElement tableData: tableRow) {
				card.setName(cardName);
				List<WebElement> td = tableData.findElements(By.tagName("td"));
				if(!(blacklist.contains(td.get(1).getText())) && td.get(0).findElement(By.tagName("a")).getText().equalsIgnoreCase(cardName)) {
					if(checkIfFoil(td.get(0))) {
						card.setName(card.getName() + " (foil)");
					} else {
						card.setName(cardName);
					}
					String keyString = Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
					td.get(0).findElement(By.tagName("a")).sendKeys(keyString);
					grabCardInfo();
				}
			}
		} catch(NoSuchElementException e) {
			card.setName(cardName);
			grabCardInfo();
		}
	}
	
	private static void grabCardInfo() {
		String originalHandle = driver.getWindowHandle();
		for(String handle : driver.getWindowHandles()) {
			try {
				driver.switchTo().window(handle);
				WebElement paperPriceBox = driver.findElement(By.className("paper"));
				String price = TextCleaner.escapeSpecialCharacters(paperPriceBox.findElement(By.className("price-box-price")).getText());
				String set = driver.findElement(By.className("price-card-name-set-name")).getText();
				String cleanName = TextCleaner.escapeSpecialCharacters(card.getName());
				Card foundCard = new Card(cleanName, set, price);
				allCards.add(foundCard);		
			} catch(NoSuchElementException e) {
				// fail gracefully
			} finally {
				if (!handle.equals(originalHandle)) {
					driver.switchTo().window(handle).close();
				}
			}
		}
		driver.switchTo().window(originalHandle);
	}
	
	private static boolean checkIfFoil(WebElement tableData) {
		try {
			tableData.findElement(By.tagName("img"));
			return true;
		} catch(NoSuchElementException e) {
			return false;
		}	
	}
}
