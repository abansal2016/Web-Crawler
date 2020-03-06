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

public class ServiceCentreStar {
  public static void getHtml(WebDriver driver, int PagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;

    String storeName, storeRating, storeReviewsCount, storeAddress;
    String baseURL = driver.getCurrentUrl();
    WebElement nextButton;

   do {
      waitForPageLoading(driver);
      List<WebElement> centreList = driver.findElements(By.className("section-result"));

      if (centreList.size() == 0) {
        System.out.println("No results found for " + baseURL);

        //Capture if there is no lost, only a single centre in a single location
        centreList = driver.findElements(By.className("section-hero-header-title-description"));

        if (centreList.size() != 0) {
          storeName = centreList.get(0).findElement(By.xpath(".//*")).getText();
          storeRating = centreList.get(0).findElement(By.className("section-star-display")).getText();
          storeReviewsCount = centreList.get(0).findElement(By.className("section-rating-term-list")).getText();
          storeAddress = driver.findElement(By.cssSelector("span[aria-label='Address']")).findElement(By.xpath(".//..//span[2]")).getText();

          System.out.println(baseURL + "@" + storeName + "@" + storeRating + "@" + storeReviewsCount + "@" + storeAddress);
        }

        return;
      }

      System.out.println("No of stores = " + centreList.size());

      for (WebElement centre: centreList) {
        storeName = centre.findElement(By.className("section-result-title")).getText();
        try {
          storeRating = centre.findElement(By.className("cards-rating-score")).getText();
          storeReviewsCount = centre.findElement(By.className("section-result-num-ratings")).getText();
        } catch (Exception e) {
          storeRating = "";
          storeReviewsCount = "";
        }
        storeAddress = centre.findElement(By.className("section-result-location")).getText();
        System.out.println(baseURL + "@" + storeName + "@" + storeRating + "@" + storeReviewsCount + "@" + storeAddress);
      }

      nextButton = driver.findElement(By.cssSelector("button[aria-label=' Next page ']"));
      System.out.println("Next button status: " + nextButton.getAttribute("disabled"));
      if (nextButton.getAttribute("disabled") == null)
        nextButton.click();
      else
        break;

    } while(true);
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
          List<WebElement> nextList = driver.findElements(By.className("section-result-dummy"));
          if (nextList.size() != 0)
            buttonStatus = true;
          //boolean buttonStatus = true;
 
          return docStatus && jQueryStatus && buttonStatus;
          //return docStatus && jQueryStatus;
        }
      });
    }
    catch (Exception e) {
      //System.out.println("Error in wating for page load" + e);
    }
  }


}
