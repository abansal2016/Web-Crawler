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
  public static void getHtml(WebDriver driver, String baseURL, int PagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;

    int storeNo = 0;
    //String baseURL = driver.getCurrentUrl();
    JavascriptExecutor jsx = (JavascriptExecutor) driver;

    do {
      waitForPageLoading(driver);
      cprint("Waiting for page load complete");
      List<WebElement> centreList = driver.findElements(By.className("section-result"));

      if (centreList.size() == 0) {
        cprint("No results found");
        getStoreDetails(driver, baseURL, storeNo);
        getAllReviews(driver, baseURL, storeNo);

        return;
      }
      cprint("No of stores = " + centreList.size());

      if (centreList.size() == storeNo) {
        cprint("All stores covered");
        return;
      }

      try {
      cprint("Clicking");
      //centreList.get(storeNo).click();
      jsx.executeScript("arguments[0].click();", centreList.get(storeNo));

      waitForPageLoading1(driver);
      cprint("Waiting for review page load complete");
      } catch(Exception e) {
        cprint("Error in clicking store: " + storeNo + e);
      }

      getStoreDetails(driver, baseURL, storeNo);

      getAllReviews(driver, baseURL, storeNo);

      //WebElement backButton = driver.findElement(By.cssSelector("button[class='section-back-to-list-button blue-link noprint']"));
      //backButton.click();
      //driver.navigate().back();
      driver.navigate().to(baseURL);
      storeNo++;

      //WebElement allReviewsButton = driver.findElements(By.cssSelector("span[class='allxGeDnJMl__text gm2-button-alt']"));
    } while(true);

    /*obj.put(driver.getCurrentUrl(), driver.getPageSource());

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
      cprint("Error in waiting for page load" + e);
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
      cprint("Error in waiting1 for page load" + e);
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
          //List<WebElement> nextList = driver.findElements(By.cssSelector("div[class='section-listbox section-scrollbox scrollable-y scrollable-show']"));
          List<WebElement> nextList = driver.findElements(By.cssSelector("div[class='section-layout section-scrollbox scrollable-y scrollable-show']"));
          if (nextList.size() != 0)
            buttonStatus = true;

          return docStatus && jQueryStatus && buttonStatus;
        }
      });
    }
    catch (Exception e) {
      cprint("Error in waiting2 for page load" + e);
    }
  }

  public static void getStoreDetails(WebDriver driver, String baseURL, int storeNo) {
    String storeName, storeRating, storeAddress, storePhone, storeReviewCount;

    try {
      storeName = driver.findElement(By.className("section-hero-header-title-title")).getText();
    } catch (Exception e) {
      storeName = "Store name not found";
    }

    try {
      storeRating = driver.findElement(By.className("section-star-display")).getText();
    } catch (Exception e) {
      storeRating = "";
    }

    try {
      //storeAddress = driver.findElement(By.cssSelector("span[aria-label='Address']")).findElement(By.xpath("./..")).getText();
      storeAddress = driver.findElement(By.cssSelector("button[data-tooltip='Copy address']"))
                           .getAttribute("aria-label")
                           .replace("Address: ", "");
    } catch (Exception e) {
      storeAddress = "Address not found";
    }

    try {
      //storePhone = driver.findElement(By.cssSelector("span[aria-label='Phone']")).findElement(By.xpath("./..")).getText();
      storePhone = driver.findElement(By.cssSelector("button[data-tooltip='Copy phone number']"))
                         .getAttribute("data-item-id")
                         .replace("phone:tel:", "");
    } catch (Exception e) {
      storePhone = "Phone number not found";
    }

    try {
      storeReviewCount = driver.findElement(By.className("section-rating-term-list"))
                               .getText()
                               .replace("(", "")
                               .replace(")", "");
    } catch (Exception e) {
     storeReviewCount = "0";
    }

    String url_print = "SR=" +
                       baseURL.replace("https://www.google.com/maps/search/", "")
                              .replace("service+centre+", "")
                              .replace("vivo", "v")
                              .replace("samsung", "s")
                              .replace("oppo", "o")
                              .replace("xiaomi", "x")
                       + "+" + storeNo;
    System.out.println(url_print + "\t" + storeName + "\t" + storePhone + "\t" + 
                       storeAddress + "\t" + storeRating + "\t" + storeReviewCount);
  }

  public static void getAllReviews(WebDriver driver, String baseURL, int storeNo) {
    try {
      WebElement allReviewsButton = driver.findElement(By.cssSelector("span[class='allxGeDnJMl__text gm2-button-alt']"));

      allReviewsButton.click();
      waitForPageLoading2(driver);

      scrollReviews(driver);
      cprint("Scrolling complete");

      List<WebElement> allReviews = driver.findElements(By.cssSelector("div[class*='section-review ripple-container']"));
      cprint("Count of reviews=" + allReviews.size());

      String url_print = baseURL.replace("https://www.google.com/maps/search/", "")
                                .replace("service+centre+", "")
                                .replace("vivo", "v")
                                .replace("samsung", "s")
                                .replace("oppo", "o")
                                .replace("xiaomi", "x")
                         + "+" + storeNo;

      for (WebElement review: allReviews) {
        System.out.println(url_print + "\t" + //storeName + "\t" + storeRating + "\t" + storePhone + "\t" +
                           review.findElement(By.className("section-review-title")).getText() + "\t" +
                           review.findElement(By.className("section-review-stars"))
                                                .getAttribute("aria-label")
                                                .replace(" stars", "")
                                                .replace(" star", "") + "\t" +
                           review.findElement(By.className("section-review-publish-date")).getText() + "\t" +
                           review.findElement(By.className("section-review-text")).getText().replace("\n", "."));
      }
    } catch (Exception e) {
      cprint("No reviews");
    }
  }

  public static void scrollReviews(WebDriver driver) {
    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    long oldReviewsHeight, newReviewsHeight = 0;
    long start;

    do {
      oldReviewsHeight = newReviewsHeight;
      jsx.executeScript("document.getElementsByClassName('section-layout section-scrollbox scrollable-y scrollable-show')[0].scrollTop=arguments[0]", oldReviewsHeight);
      start = System.currentTimeMillis();
      do {
        newReviewsHeight = (long) jsx.executeScript("return document.getElementsByClassName('section-layout section-scrollbox scrollable-y scrollable-show')[0].scrollHeight");
        if (oldReviewsHeight != newReviewsHeight) //height changed = scroll down complete, therefore break the while loop
          break;
      } while (System.currentTimeMillis() - start < 3000); //wait for 3 seconds to load the next set of reviews
    } while(oldReviewsHeight != newReviewsHeight); //height not changed = no further reviews, therefore all reviews loaded, break the while loop

  }

  public static void cprint(String msg) {
    //System.out.println(msg); 
  }

}
