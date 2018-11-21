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

public class Google {
  public static void getHtml(WebDriver driver, int PagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;
    
    List<WebElement> allReviewsButton = driver.findElements(By.cssSelector("a[data-reply_source='local-search-reviews-list']"));
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
    }

    //driver.quit();
  }

  public static void waitForPageLoading(WebDriver driver){
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
          List<WebElement> nextList = driver.findElements(By.cssSelector("div[class='lcorif fp-w']"));
          if (nextList.size() != 0)
            buttonStatus = true;

          return docStatus && jQueryStatus && buttonStatus;
          //return docStatus && jQueryStatus;
        }
      });
    }
    catch (Exception e) {
      System.out.println("Error in wating for page load" + e);
    }
  }
}
