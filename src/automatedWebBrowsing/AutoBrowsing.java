package automatedWebBrowsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class AutoBrowsing {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ArrayList<String> blacklist = Read.readTxtFile("blacklist.txt");
		ArrayList<String> cardList = Read.readTxtFile(args[0]);
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		
		 ChromeOptions options = new ChromeOptions();
		 options.addArguments("headless");

		//Initiating  chromedriver
		WebDriver driver= new ChromeDriver(options);

		//Applied wait time
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		//maximise window
		driver.manage().window().maximize();
			
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
					if((blacklist.contains(td.get(1).getText())) == false && td.get(0).findElement(By.tagName("a")).getText().equals(card)) {
						String keyString = Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
						td.get(0).findElement(By.tagName("a")).sendKeys(keyString);
					};
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
			            	cardData.add(escapeSpecialCharacters(card));
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
	
	private static String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
}
