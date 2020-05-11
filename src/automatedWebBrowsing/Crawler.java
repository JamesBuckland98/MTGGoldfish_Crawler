package automatedWebBrowsing;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import helper.TextCleaner;
import model.Card;

public class Crawler {
	
	private static ArrayList<Card> allCards = new ArrayList<Card>();
	private static String cardName;
	private static WebDriver driver;
	private static Card card = new Card();;

	public static ArrayList<Card> searchForCard(String cardName, ArrayList<String> blacklist, WebDriver driver) {
		Crawler.driver = driver;
		Crawler.cardName = cardName;
		System.out.println("Seaching for card: " + cardName + "...");
		driver.get("https://www.mtggoldfish.com/");
		WebElement searchBar = driver.findElement(By.id("query_string"));
		searchBar.sendKeys(cardName);
		searchBar.sendKeys(Keys.ENTER);
		findTable(blacklist);
		return allCards;
	}
	
	private static void findTable(ArrayList<String> blacklist) {
		card.setName(cardName.trim());
		try {
			WebElement table = driver.findElement(By.className("table-striped"));
			List<WebElement> tableRow = table.findElements(By.tagName("tr"));
			for(WebElement tableData: tableRow) {
				List<WebElement> td = tableData.findElements(By.tagName("td"));
				if((blacklist.contains(td.get(1).getText())) == false && td.get(0).findElement(By.tagName("a")).getText().equalsIgnoreCase(cardName)) {
					if(checkIfFoil(td.get(0))) {
						card.setName(card.getName() + " (foil)");
					}
					String keyString = Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
					td.get(0).findElement(By.tagName("a")).sendKeys(keyString);
					grabCardInfo();
				}
			}
		} catch(Exception e) {
			grabCardInfo();
		}
	}
	
	private static void grabCardInfo() {
		String originalHandle = driver.getWindowHandle();
		for(String handle : driver.getWindowHandles()) {
			try {
				driver.switchTo().window(handle);
				WebElement paperPriceBox = driver.findElement(By.className("paper"));
				String price = paperPriceBox.findElement(By.className("price-box-price")).getText();
				String set = driver.findElement(By.className("price-card-name-set-name")).getText();
				String cleanName = TextCleaner.escapeSpecialCharacters(card.getName());
				Card foundCard = new Card(cleanName, set, price);
				allCards.add(foundCard);
				if (!handle.equals(originalHandle)) {
					driver.switchTo().window(handle).close();
				}				
			} catch(NoSuchElementException e) {
				if (!handle.equals(originalHandle)) {
					driver.switchTo().window(handle).close();
				}
			} 
			driver.switchTo().window(originalHandle);
	     }
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
