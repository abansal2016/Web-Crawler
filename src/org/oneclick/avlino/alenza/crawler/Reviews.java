package org.oneclick.avlino.alenza.crawler;

import org.oneclick.avlino.alenza.domain.*;

import org.openqa.selenium.WebDriver;

public class Reviews extends Thread {
  WebDriver driver;
  String url;
  String outputPath;

  Reviews(WebDriver drvr, String inputUrl, String oPath) {
    driver = drvr;
    url = inputUrl;
    outputPath = oPath;
  }

  public void run() {
    try {
    driver.get(url);
    //Google.getHtml(driver, 5, outputPath);
    new Amazon().getHtml(driver, 1000, outputPath);
    //driver.quit();
    System.out.println("Loading complete of URL: " + url);
    } catch(Exception e) {
      System.out.println("Thread error in loading URL: " + url);
      System.out.println("Thread error \n" + e);
    }
    driver.quit();

/*    String domain = null;
    try {
      domain = (new URL(url)).getHost().toLowerCase();
      System.out.println("domain= " + domain);
    } catch(Exception e) {
      System.out.println("Incorrect url " + e);
    }

    if (domain.equals("google.co.in") || domain.equals("google.com"))
      Google.getHtml(driver, 5, outputPath);*/
  }
}
