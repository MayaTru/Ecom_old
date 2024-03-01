package org.maya;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Test3 {
	@Test
	public void login() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\JarsForTestAut\\driver6\\chromedriver.exe");
		ChromeOptions opt = new ChromeOptions();
		opt.addArguments("--remote-allow-origins=*");
		opt.setBinary("C:\\JarsForTestAut\\chrome-win64\\chrome-win64\\chrome.exe");
		WebDriver driver = new ChromeDriver(opt);
		String username = "mayatru@mail.com";
		String password = "Maya@0812";
		driver.get("https://rahulshettyacademy.com/client");
		driver.findElement(By.id("userEmail")).sendKeys(username);
		driver.findElement(By.cssSelector("#userPassword")).sendKeys(password);
		driver.findElement(By.xpath("//input[@id='login']")).click();
		AddItem(driver);
		CheckCart(driver);
		Checkout(driver);
		List<String> oid = PlaceOrder(driver);
		CheckOrder(driver,oid);
	}
	public void AddItem(WebDriver driver) throws InterruptedException{
		String[] wp = {"ZARA COAT 3","ADIDAS ORIGINAL"};
		List<String> wantpro = Arrays.asList(wp);
		WebDriverWait w1 = new WebDriverWait(driver, Duration.ofSeconds(5));
		w1.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.card-body")));
		List<WebElement> lst  = driver.findElements(By.cssSelector("div.card-body"));
		for(int i=0;i<lst.size();i++) {
			String pro = lst.get(i).findElement(By.cssSelector("h5 b")).getText();
			if(wantpro.contains(pro)) {
				Thread.sleep(3000);
				lst.get(i).findElement(By.xpath("button[2]")).click();
			}
		}
	}
	public void CheckCart(WebDriver driver) throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/cart']")).click();
		Thread.sleep(3000);
		List<WebElement> lst = driver.findElements(By.cssSelector(".cart ul li div:nth-child(2)"));
		long totalch = 0;
		for(WebElement l : lst) {
			String[] temp = l.getText().split(" ");
			long pcost = Long.parseLong(temp[1].trim());
			totalch += pcost;	
		}
		String temp = driver.findElement(By.xpath("//li[@class='totalRow']/span[text()='Total']/following-sibling::span")).getText();
		String temp2 = temp.substring(1,temp.length());
		long total = Long.parseLong(temp2);
		Assert.assertEquals(total, totalch);
	}
	public void Checkout(WebDriver driver) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scroll(0,500)");
		Thread.sleep(3000);
		js.executeScript("window.scroll(500,1000)");
		WebDriverWait w2 = new WebDriverWait(driver, Duration.ofSeconds(5));
		w2.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='totalRow']/button[text()='Checkout']")));
		driver.findElement(By.xpath("//li[@class='totalRow']/button[text()='Checkout']")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("div.form-group input")).sendKeys("Ind");
		Thread.sleep(2000);
		List<WebElement> lst = driver.findElements(By.cssSelector("div.form-group section button span"));
		for(int i=0;i<lst.size();i++) {
			String temp = lst.get(i).getText();
			if(temp.equalsIgnoreCase("India")) {
				lst.get(i).click();
				break;
			}
		}
	}
	public List<String> PlaceOrder(WebDriver driver) throws InterruptedException {
		driver.findElement(By.cssSelector("div.actions a")).click();
		WebDriverWait w3 = new WebDriverWait(driver, Duration.ofSeconds(5));
		w3.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ng-star-inserted td label.ng-star-inserted")));
		List<WebElement> lst = driver.findElements(By.cssSelector(".ng-star-inserted td label.ng-star-inserted"));
		List<String> oid = new ArrayList<String>();
		for(int i=0;i<lst.size();i++) {
			String temp = lst.get(i).getText();
			String[] temp2 = temp.split(" ");
			oid.add(temp2[1]);
		}
		return oid;
	}
	public void CheckOrder(WebDriver driver, List<String> oid) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scroll(1000,500)");;
		js.executeScript("window.scroll(500,0)");
		Thread.sleep(1000);
		js.executeScript("window.scroll(0,0)");
		WebDriverWait w4 = new WebDriverWait(driver, Duration.ofSeconds(5));
		w4.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[1]/li[3]/button[1]")));
		driver.findElement(By.xpath("//ul[1]/li[3]/button[1]")).click();
		Thread.sleep(3000);
		List<WebElement> lst = driver.findElements(By.xpath("//table/tbody/tr/th"));
		System.out.println(lst.size());
		for(int i=0;i<lst.size();i++) {
			if(oid.contains(lst.get(i).getText())) {
				System.out.println(lst.get(i).getText());
			}
		}
		
	}
}
