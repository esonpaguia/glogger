package com.telus.glogger;

import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

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

			if (args.length > 0) {
				String filePath = args[0];
				logger.info("Reading " + filePath);
				input = new FileInputStream(filePath);
			} else {
				throw new Exception("Config file path is missing.");
			}

			/*
			 * else { input =
			 * ClassLoader.getSystemResourceAsStream("config.properties");
			 * logger.info("Reading classpath:config.properties"); }
			 */

			config.load(input);

			String username = config.getProperty(USERNAME);
			String password = config.getProperty(PASSWORD);
			String url = config.getProperty(URL);
			boolean toCloseWindow = Boolean.getBoolean(config
					.getProperty(TO_CLOSE_WINDOW));

			if (StringUtils.isEmpty(username)) {
				throw new Exception("username is empty.");
			}

			if (StringUtils.isEmpty(password)) {
				throw new Exception("password is empty.");
			}

			if (StringUtils.isEmpty(url)) {
				throw new Exception("url is empty.");
			}

			logger.debug(USERNAME + "=" + username);
			logger.debug(PASSWORD + "=" + password);
			logger.debug(URL + "=" + url);
			logger.debug(TO_CLOSE_WINDOW + "=" + toCloseWindow);

			logger.info("Checking in...");

			GLogger g = new GLogger();

			g.checkin(url, username, password, toCloseWindow);

		} catch (Exception e) {
			logger.error(e);
		} finally {
			System.exit(0);
		}
	}

	@SuppressWarnings("unused")
	private FirefoxProfile getProfile(){
		// TODO: Set different IP address
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.proxy.type", 1);
		profile.setPreference("network.proxy.http", "142.175.233.89");
		profile.setPreference("network.proxy.http_port", "8080");
		return profile;
	}

	public void checkin(String url, String username, String password,
			boolean toCloseWindow) throws InterruptedException {

		try {

			driver = new FirefoxDriver();
			baseUrl = url;
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			driver.get(baseUrl);
			driver.findElement(By.id("loggin-input_username")).clear();
			driver.findElement(By.id("loggin-input_username")).sendKeys(username);
			driver.findElement(By.id("loggin-input_password")).clear();
			driver.findElement(By.id("loggin-input_password")).sendKeys(password);
			driver.findElement(By.id("loggin-input_username")).click();

			Thread.sleep(4000);
            
            WebElement loggerGroup = driver.findElement(By.id("loggin-select_instance"));
        	Select loggerGroupOption = new Select(loggerGroup);
            loggerGroupOption.selectByVisibleText("Ortigas Center(AGT)");

            WebElement loggerSubGroup = driver.findElement(By.id("loggin-select_subgroup"));
            Select loggerSubGroupOption = new Select(loggerSubGroup);
            loggerSubGroupOption.selectByVisibleText("TELUS ITO-App Devt & Support"); 
            
            WebElement loggerStatus = driver.findElement(By.id("loggin-select_status"));
            Select loggerStatusOption = new Select(loggerStatus);
            loggerStatusOption.selectByVisibleText("Clock In");
            
			driver.findElement(By.id("loggin-input_accept")).click();
			
			Thread.sleep(4000);
			
			try {
				Assert.assertEquals("You are signed in as: " + username
						+ " (Ortigas Center) | Tutorial | Sign out", driver
						.findElement(By.cssSelector("#loggin > div")).getText());
				logger.info("Successfully checked in.");
			} catch (Error e) {
				verificationErrors.append(e.toString());
			}

			String verificationErrorString = verificationErrors.toString();
			if (!"".equals(verificationErrorString)) {
				logger.error(verificationErrorString);
			} 

		} finally {
			if (toCloseWindow)
				driver.quit();
		}

	}

}