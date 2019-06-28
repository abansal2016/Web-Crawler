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

public class ServiceCentre {
  public static void getHtml(WebDriver driver, int PagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;

    int storeNo = 0;
    boolean newStoreFound = true;
    String baseURL = driver.getCurrentUrl();

    do {
      waitForPageLoading(driver);
      //System.out.println("Waiting for page load complete");
      List<WebElement> centreList = driver.findElements(By.className("section-result"));

      if (centreList.size() == 0) {
        System.out.println("No results found");
        return;
      }
      //System.out.println("No of stores = " + centreList.size());

      if (centreList.size() == storeNo) {
        System.out.println("All stores covered");
        return;
      }

      String storeName, storeRating, storeAddress, storePhone;

      storeName = centreList.get(storeNo).findElement(By.className("section-result-title")).getText();
      //System.out.println("Store Name = " + storeName);

      try {
        storeRating = centreList.get(storeNo).findElement(By.className("cards-rating-score")).getText();
        //System.out.println("Store Rating = " + storeRating);
      } catch (Exception e) {
          storeRating = "";
      }

      centreList.get(storeNo).click();
      waitForPageLoading1(driver);
      //System.out.println("Waiting for review page load complete");

      storeAddress = driver.findElement(By.cssSelector("span[aria-label='Address']")).findElement(By.xpath("./..")).getText();
      //System.out.println("Store Address = " + storeAddress);

      storePhone = driver.findElement(By.cssSelector("span[aria-label='Phone']")).findElement(By.xpath("./..")).getText();
      //System.out.println("Store Phone = " + storePhone);

      System.out.println(storeName + "\t" + storePhone + "\t" + storeAddress + "\t" + storeRating);

      try {
        WebElement allReviewsButton = driver.findElement(By.cssSelector("span[class='allxGeDnJMl__text gm2-button-alt']"));
        allReviewsButton.click();

        waitForPageLoading2(driver);
        scrollReviews(driver);

        List<WebElement> allReviews = driver.findElements(By.cssSelector("div[class='section-review ripple-container']"));
        for (WebElement review: allReviews) {
          /*System.out.println("\nReviewer Name: " + review.findElement(By.className("section-review-title")).getText());
          System.out.println("Review star: " + review.findElement(By.className("section-review-stars")).getAttribute("aria-label"));
          System.out.println("Review Date: " + review.findElement(By.className("section-review-publish-date")).getText());
          System.out.println("Review: " + review.findElement(By.className("section-review-text")).getText());*/

          System.out.println("\t" + //storeName + "\t" + storeRating + "\t" + storePhone + "\t" +
                             review.findElement(By.className("section-review-title")).getText() + "\t" +
                             review.findElement(By.className("section-review-stars")).getAttribute("aria-label") + "\t" +
                             review.findElement(By.className("section-review-publish-date")).getText() + "\t" +
                             review.findElement(By.className("section-review-text")).getText().replace("\n", "."));
        }
      } catch (Exception e) {
          System.out.println("No reviews");
      }

      //WebElement backButton = driver.findElement(By.cssSelector("button[class='section-back-to-list-button blue-link noprint']"));
      //backButton.click();
      //driver.navigate().back();
      //driver.navigate().back();
      driver.navigate().to(baseURL);
      storeNo++;

      //WebElement allReviewsButton = driver.findElements(By.cssSelector("span[class='allxGeDnJMl__text gm2-button-alt']"));
    } while(true);

    /*List<WebElement> allReviewsButton = driver.findElements(By.cssSelector("a[data-reply_source='local-search-reviews-list']"));
    if (allReviewsButton.size() == 0)
      return;

    allReviewsButton.get(0).click();
    waitForPageLoading(driver);

    JavascriptExecutor jsx = (JavascriptExecutor) driver;

    long oldReviewsHeight, newReviewsHeight = 0;
    long start;

    do {
      oldReviewsHeight = newReviewsHeight;
      jsx.executeScript("document.getElementsByClassName('review-dialog-list')[0].scrollTop=arguments[0]", oldReviewsHeight);
      start = System.currentTimeMillis();
      do {
        newReviewsHeight = (long) jsx.executeScript("return document.getElementsByClassName('review-dialog-list')[0].scrollHeight");
        if (oldReviewsHeight != newReviewsHeight) //height changed = scroll down complete, therefore break the while loop
          break;
      } while (System.currentTimeMillis() - start < 3000); //wait for 3 seconds to load the next set of reviews
    } while(oldReviewsHeight != newReviewsHeight); //height not changed = no further reviews, therefore all reviews loaded, break the while loop

    obj.put(driver.getCurrentUrl(), driver.getPageSource());

    try {
      FileWriter file = new FileWriter(outputPath + "file" + fileNo);
      file.write(obj.toJSONString());
      file.flush();
      file.close();
    } catch (Exception e) {
      System.out.println("error in writing file" + e);
    }*/

    //driver.quit();
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
          List<WebElement> nextList = driver.findElements(By.className("section-result"));
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
