package org.oneclick.avlino.alenza.domain;

import java.io.FileWriter;
import java.util.List;
import java.util.function.Function;

import org.json.simple.JSONObject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServiceCentreNew {
  public static void getHtml(WebDriver driver, int PagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;

    //int storeNo = 0;
    //boolean newStoreFound = true;
    String storeName, storeRating, storeAddress, storePhone, storeReviewCount;
    String baseURL = driver.getCurrentUrl();

    waitForPageLoading(driver);
    System.out.println("Waiting for page load complete for new service centre code");
    List<WebElement> centreList = driver.findElements(By.cssSelector("div[class='iXbUVLX_QpxQ-ptqfrjbX76M r-ih15l77Ld4f8']"));

    if (centreList.size() == 0) {
      System.out.println("No results found");
      return;
    }
    System.out.println("No of stores = " + centreList.size());

    for (WebElement centre: centreList) {
      centre.click();
      storeName = driver.findElement(By.cssSelector("div[class='kno-ecr-pt PZPZlf gsmt PPT5v hNKfZe']")).getText();
      storeRating = driver.findElement(By.className("Aq14fc")).getText();
      storeReviewCount = driver.findElement(By.className("r-iYVCsEFkx4zk")).getText();
      storeAddress = driver.findElement(By.cssSelector("div[class='zloOqf PZPZlf']")).getText();
      storePhone = driver.findElement(By.cssSelector("div[class='LrzXr zdqRlf kno-fv']")).getText();
      System.out.println(storeName + "\t" + storeRating + "\t" + storeReviewCount + "\t" + storePhone + "\t" + storeAddress);
    }
  }

  public static void waitForPageLoading(WebDriver driver){
    try {
      new WebDriverWait(driver, 20).until(new Function<WebDriver, Boolean>() {
        @Override
        public Boolean apply(WebDriver driver) {
          JavascriptExecutor jsx = (JavascriptExecutor) driver;
          boolean docStatus = (boolean) jsx.executeScript("return document.readyState=='complete'");

          boolean jQueryStatus = (boolean) jsx.executeScript("return typeof(jQuery)=='undefined'");
          if (!jQueryStatus)
            jQueryStatus = (boolean) jsx.executeScript("return jQuery.active==0");

          boolean buttonStatus = false;
          List<WebElement> nextList = driver.findElements(By.cssSelector("div[class='iXbUVLX_QpxQ-ptqfrjbX76M r-ih15l77Ld4f8']"));
          if (nextList.size() != 0)
            buttonStatus = true;
          //boolean buttonStatus = true;
 
          return docStatus && jQueryStatus && buttonStatus;
          //return docStatus && jQueryStatus;
        }
      });
    }
    catch (Exception e) {
      System.out.println("Error in wating for page load" + e);
    }
  }

  public static void waitForPageLoading1 (WebDriver driver) {
    try {
      new WebDriverWait(driver, 10).until(new Function<WebDriver, Boolean>() {
        @Override
        public Boolean apply(WebDriver driver) {
          JavascriptExecutor jsx = (JavascriptExecutor) driver;
          boolean docStatus = (boolean) jsx.executeScript("return document.readyState=='complete'");

          boolean jQueryStatus = (boolean) jsx.executeScript("return typeof(jQuery)=='undefined'");
          if (!jQueryStatus)
            jQueryStatus = (boolean) jsx.executeScript("return jQuery.active==0");

          boolean buttonStatus = false;
          List<WebElement> nextList = driver.findElements(By.cssSelector("button[class='section-back-to-list-button blue-link noprint']"));
          if (nextList.size() != 0)
            buttonStatus = true;
          //boolean buttonStatus = true;

          return docStatus && jQueryStatus && buttonStatus;
        }
      });
    }
    catch (Exception e) {
      System.out.println("Error in wating for page load" + e);
    }
  }

  public static void waitForPageLoading2 (WebDriver driver) {
    try {
      new WebDriverWait(driver, 10).until(new Function<WebDriver, Boolean>() {
        @Override
        public Boolean apply(WebDriver driver) {
          JavascriptExecutor jsx = (JavascriptExecutor) driver;
          boolean docStatus = (boolean) jsx.executeScript("return document.readyState=='complete'");

          boolean jQueryStatus = (boolean) jsx.executeScript("return typeof(jQuery)=='undefined'");
          if (!jQueryStatus)
            jQueryStatus = (boolean) jsx.executeScript("return jQuery.active==0");

          boolean buttonStatus = false;
          List<WebElement> nextList = driver.findElements(By.cssSelector("div[class='section-listbox section-scrollbox scrollable-y scrollable-show']"));
          if (nextList.size() != 0)
            buttonStatus = true;

          return docStatus && jQueryStatus && buttonStatus;
        }
      });
    }
    catch (Exception e) {
      System.out.println("Error in wating for page load" + e);
    }
  }

  public static void scrollReviews(WebDriver driver) {
    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    long oldReviewsHeight, newReviewsHeight = 0;
    long start;

    do {
      oldReviewsHeight = newReviewsHeight;
      jsx.executeScript("document.getElementsByClassName('section-listbox section-scrollbox scrollable-y scrollable-show')[0].scrollTop=arguments[0]", oldReviewsHeight);
      start = System.currentTimeMillis();
      do {
        newReviewsHeight = (long) jsx.executeScript("return document.getElementsByClassName('section-listbox section-scrollbox scrollable-y scrollable-show')[0].scrollHeight");
        if (oldReviewsHeight != newReviewsHeight) //height changed = scroll down complete, therefore break the while loop
          break;
      } while (System.currentTimeMillis() - start < 3000); //wait for 3 seconds to load the next set of reviews
    } while(oldReviewsHeight != newReviewsHeight); //height not changed = no further reviews, therefore all reviews loaded, break the while loop

  }

}
