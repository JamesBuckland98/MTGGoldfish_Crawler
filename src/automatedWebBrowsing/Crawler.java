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

	public static ArrayList<Card> searchForCard(String cardName, ArrayList<String> blacklist, WebDriver driver) {
		Crawler.cardName = cardName;
		System.out.println("Seaching for card: " + cardName + "...");
		driver.get("https://www.mtggoldfish.com/");
		WebElement searchBar = driver.findElement(By.id("query_string"));
		searchBar.sendKeys(cardName);
		searchBar.sendKeys(Keys.ENTER);
		findTable(blacklist, driver);
		grabCardInfo(driver);
		return allCards;
	}
	
	private static void findTable(ArrayList<String>blacklist, WebDriver driver) {
		try {
			WebElement table = driver.findElement(By.className("table-striped"));
			List<WebElement> tableRow = table.findElements(By.tagName("tr"));
			for(WebElement tableData: tableRow) {
				List<WebElement> td = tableData.findElements(By.tagName("td"));
				if((blacklist.contains(td.get(1).getText())) == false && td.get(0).findElement(By.tagName("a")).getText().equalsIgnoreCase(Crawler.cardName.trim())) {
					String keyString = Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
					td.get(0).findElement(By.tagName("a")).sendKeys(keyString);
				}
			}
		} catch(NoSuchElementException e) {
			return;
		}
	}
	
	private static void grabCardInfo(WebDriver driver) {
		String originalHandle = driver.getWindowHandle();
		for(String handle : driver.getWindowHandles()) {
			try {
				driver.switchTo().window(handle);
				List<WebElement> paperPriceBox = driver.findElements(By.className("paper"));
				if(paperPriceBox.size() != 0) {
					String price = paperPriceBox.get(0).findElement(By.className("price-box-price")).getText();
					String set = driver.findElement(By.className("price-card-name-set-name")).getText();
					Card foundCard = new Card(TextCleaner.escapeSpecialCharacters(cardName), set, price);
					allCards.add(foundCard);
				}				
			} catch(NoSuchElementException e) {
				if (!handle.equals(originalHandle)) {
					driver.close();
				}
			} finally {
				if (!handle.equals(originalHandle)) {
					driver.close();
				}
			}
	     }
	     driver.switchTo().window(originalHandle);
	}
}
