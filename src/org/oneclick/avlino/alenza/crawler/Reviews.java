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
    Tripadvisor.getHtml(driver, 10, outputPath);
    driver.quit();
    } catch(Exception e) {
      System.out.println("Thread error" + e);
    }

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
