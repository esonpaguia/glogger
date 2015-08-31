package com.telus.glogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Eson Paguia
 * 
 */
public final class GLogger {

	private final static Logger logger = Logger.getLogger(GLogger.class);
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String URL = "url";
	public static final String TO_CLOSE_WINDOW = "toCloseWindow";

	public static void main(String[] args) {
		
		Properties config = new Properties();
		InputStream input = null;
		
		try {
			
			if (args.length > 0){
				String filePath = args[0];
				logger.info("Reading " + filePath);
				input = new FileInputStream(filePath);
			} else {
				throw new Exception("Config file path is invalid.");
			}
			
			/*else {
			 	input = ClassLoader.getSystemResourceAsStream("config.properties");
				logger.info("Reading classpath:config.properties");
			}*/
			
			config.load(input);
			
			String username = config.getProperty(USERNAME);
			String password = config.getProperty(PASSWORD);
			String url = config.getProperty(URL);
			boolean toCloseWindow = Boolean.getBoolean(config.getProperty(TO_CLOSE_WINDOW));
			
			if (StringUtils.isEmpty(username)){
				logger.error("username is invalid");
			}
			
			if (StringUtils.isEmpty(password)){
				logger.error("password is invalid");
			}
			
			if (StringUtils.isEmpty(url)){
				logger.error("url is invalid");
			}
			
			logger.debug("username="+username);
			logger.debug("password="+password);
			logger.debug("url="+url);
			logger.debug("toCloseWindow="+toCloseWindow);
			
			logger.info("Checking in...");
			
			GLogger g = new GLogger();
			
			g.checkin(url, username, password, toCloseWindow);
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			System.exit(0);
		}
	}
	
	public void checkin(String url, String username, String password, boolean toCloseWindow) throws InterruptedException {
		
		try {
			driver = new FirefoxDriver();
			baseUrl = url;
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
			driver.get(baseUrl);
			driver.findElement(By.id("loggin-input_username")).clear();
			driver.findElement(By.id("loggin-input_username")).sendKeys(
					"eddison.paguia");
			driver.findElement(By.id("loggin-input_password")).clear();
			driver.findElement(By.id("loggin-input_password"))
					.sendKeys("Password1");
			driver.findElement(By.id("loggin-input_username")).click();
	
			Thread.sleep(5000);
	
			driver.findElement(By.id("loggin-input_accept")).click();
	
			for (int second = 0;; second++) {
				if (second >= 60) {
					verificationErrors.append("timeout");
				}
				try {
					if ("Global Logger".equals(driver.getTitle()))
						break;
				} catch (Exception e) {
				}
				Thread.sleep(1000);
			}
	
			try {
				Assert.assertEquals(
						"You are signed in as: " + username + " (Ortigas Center) | Tutorial | Sign out",
						driver.findElement(By.cssSelector("#loggin > div"))
								.getText());
			} catch (Error e) {
				verificationErrors.append(e.toString());
			}
			
			String verificationErrorString = verificationErrors.toString();
			if (!"".equals(verificationErrorString)) {
				logger.error(verificationErrorString);
			}else{
				logger.info("Successfully checked in.");
			}
			
		} finally {
			if (toCloseWindow)
				driver.quit();
		}
		
	}

}