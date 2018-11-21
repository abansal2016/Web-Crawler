package org.oneclick.avlino.alenza.crawler;

import org.oneclick.avlino.alenza.domain.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class HelloWorld {
  public static void main(String[] args) {

    if (args.length < 2) {
      System.out.println("Illegal number of arguments");
      return;
    }

    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(new File(args[0])));
    } catch (Exception e) {
      System.out.println("Error in opening input URL file" + e);
    }

    //String url = "https://www.google.com/search?q=drunkart&ie=utf-8&oe=utf-8&client=firefox-b-ab";

    boolean enableFlashPlayer = false;
    int loadStylesheet = 2;
    int loadImage = 2;
    String seleniumGridBinary = "/home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/firefox/firefox";
    boolean makeHeadless = false;

    String seleniumHubProtocol = "http";
    String seleniumHubHost = "localhost";
    int seleniumHubPort = 4444;
    String seleniumHubPath = "/wd/hub";

    FirefoxOptions options = new FirefoxOptions();
    options.addPreference("dom.ipc.plugins.enabled.libflashplayer.so", enableFlashPlayer);
    options.addPreference("permissions.default.stylesheet", loadStylesheet);
    options.addPreference("permissions.default.image", loadImage);

    options.setBinary(seleniumGridBinary);
    options.setAcceptInsecureCerts(true);
    options.setHeadless(makeHeadless);

    System.setProperty("webdriver.reap_profile", "false");
    WebDriver driver = null;
    try {
      //driver = new RemoteWebDriver(new URL(seleniumHubProtocol, seleniumHubHost, seleniumHubPort, seleniumHubPath), options);
    } catch (Exception e) {
    }

    String outputPath = args[1];
    String url;
    int urlNo = 1;
    try {
      while ((url = br.readLine()) != null) {
        try {
          System.out.println("Opening URL" + urlNo + ": " + url);
          driver = new RemoteWebDriver(new URL(seleniumHubProtocol, seleniumHubHost, seleniumHubPort, seleniumHubPath), options);
          new Reviews(driver, url, outputPath + "/URL" + urlNo++).start();
          //collectReviews(driver, url, outputPath + "/URL" + urlNo++);
        } catch (Exception e) {
          System.out.println("Error in main process: initializing webdriver " + e);
        }
      }
    } catch (Exception e) {
      System.out.println("Error in main process: reading file" + e);
    }

    //driver.get(url);
    //Google.getHtml(driver, 5, "/home/abhishek.bansal/java-prog/data");

    //String innerHtml = driver.findElement(By.tagName("body")).getAttribute("innerHTML");
    //System.out.println(innerHtml);

    //System.out.println("Quiting the driver");
    //driver.quit();
  }

  public static void collectReviews(WebDriver driver, String url, String outputPath) {
    /*try {
      String domain = URLUtil.getDomainName(URL).toLowerCase();
      if(domain.equals("google.com") || domain.equals("google.co.in"))
        Google.getHtml(driver, 5, "/home/abhishek.bansal/java-prog/data");
    }
    catch(MalformedURLException e) {
      System.out.println("URL " + url + " is not correct");
    }*/

    driver.get(url);
    String domain = null;
    try {
      domain = (new URL(url)).getHost().toLowerCase();
      System.out.println("domain= " + domain);
    } catch(Exception e) {
      System.out.println("Incorrect url " + e);
    }

    if (domain.equals("google.co.in") || domain.equals("google.com"))
      Google.getHtml(driver, 5, outputPath);

  }
}
