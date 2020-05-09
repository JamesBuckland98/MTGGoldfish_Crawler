package automatedWebBrowsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import helper.Read;
import helper.TextCleaner;
import helper.Write;
import setUp.Config;

public class AutoBrowsing {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ArrayList<String> blacklist = Read.readTxtFile("blacklist.txt");
		ArrayList<String> cardList = Read.readTxtFile(args[0]);
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		WebDriver driver = Config.setUp();
		for(String card: cardList) {
			try {
				System.out.println("Seaching for card: " + card + "...");
				driver.get("https://www.mtggoldfish.com/");
				WebElement searchBar = driver.findElement(By.id("query_string"));
				searchBar.sendKeys(card);
				searchBar.sendKeys(Keys.ENTER);
				WebElement table = driver.findElement(By.className("table-striped"));
				List<WebElement> tableRow = table.findElements(By.tagName("tr"));
				for(WebElement tableData: tableRow) {
					List<WebElement> td = tableData.findElements(By.tagName("td"));
					if((blacklist.contains(td.get(1).getText())) == false && td.get(0).findElement(By.tagName("a")).getText().equals(card.trim())) {
						String keyString = Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
						td.get(0).findElement(By.tagName("a")).sendKeys(keyString);
					}
				}
				String originalHandle = driver.getWindowHandle();
				for(String handle : driver.getWindowHandles()) {
			        if (!handle.equals(originalHandle)) {
			            driver.switchTo().window(handle);
			            List<WebElement> paperPriceBox = driver.findElements(By.className("paper"));
			            if(paperPriceBox.size() != 0) {
			            	ArrayList<String> cardData = new ArrayList<String>();
			            	String price = paperPriceBox.get(0).findElement(By.className("price-box-price")).getText();
			            	String set = driver.findElement(By.className("price-card-name-set-name")).getText();
			            	cardData.add(TextCleaner.escapeSpecialCharacters(card));
			            	cardData.add(set);
			            	cardData.add(price);
			            	data.add(cardData);
			            }
			            driver.close();		            	
			        }
			    }
				driver.switchTo().window(originalHandle);
			} catch(Exception e) {
				System.out.println("error " + e);
			} finally {
				System.out.println(card + " search complete!");
				TimeUnit.SECONDS.sleep(3);
			}
		}
		System.out.println("All cards searched!");
		Write.writeToCsv(data, args[1]);
		
		//closing the browser
		driver.quit(); 
	}
}
