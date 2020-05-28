package setUp;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Config {
	public static WebDriver setUp(String headlessMode) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(headlessMode);

		//Initiating  chromedriver
		WebDriverManager.chromedriver().setup();
		WebDriver driver= new ChromeDriver(options);
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		return driver;		
	}
	
	public static WebDriver setUp() {

		//Initiating  chromedriver
		WebDriverManager.chromedriver().setup();
		WebDriver driver= new ChromeDriver();
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		return driver;		
	}
}
