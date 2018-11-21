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

public class Tripadvisor {
  public static void getHtml(WebDriver driver, int pagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;
    int pageNo = 1;
    int pageCount = 0;

    //JavascriptExecutor jsx = (JavascriptExecutor) driver;
    String reviews;
    try {
      do {
        clickOnMore(driver);
        //reviews = (String) jsx.executeScript("return document.getElementById('REVIEWS').outerHTML");
        System.out.println("ASHU: " + driver.getCurrentUrl() + "-->AlenzaCustomPageNo=" + pageNo);
        //obj.put(driver.getCurrentUrl() + "-->AlenzaCustomPageNo=" + pageNo++, reviews);
        obj.put(driver.getCurrentUrl() + "-->AlenzaCustomPageNo=" + pageNo++, driver.getPageSource());
        pageCount++;
        if (pageCount == pagesToStore) {
          dumpHtmlToFile(obj, outputPath, fileNo++);
          obj = new JSONObject();
          pageCount = 0;
        }
      } while(getNextButton(driver));

      if (pageCount > 0)
        dumpHtmlToFile(obj, outputPath, fileNo);

    } catch (Exception e) {
      System.out.println("ASHU: error in main loop" + e);
    }
  }

  public static void dumpHtmlToFile(JSONObject obj, String outputPath, int fileNo) {
    try {
      FileWriter file = new FileWriter(outputPath + "file" + fileNo);
      String newLine = System.getProperty("line.separator");

      for (Object jsonHtmlUrl: obj.keySet()) {
        JSONObject record = new JSONObject();
        record.put((String) jsonHtmlUrl, (String) obj.get(jsonHtmlUrl));
        file.write(record.toJSONString() + newLine);
      }
      //file.write(obj.toJSONString());
      file.flush();
      file.close();
    } catch (Exception e) {
      System.out.println("error in writing file" + e);
    }
  }

  public static void clickOnMore(WebDriver driver) {
    try {
      new WebDriverWait(driver, 10).until(new Function<WebDriver, Boolean>() {
        @Override
        public Boolean apply(WebDriver driver) {
          try {
            List<WebElement> moreButton = driver.findElement(By.id("REVIEWS")).findElements(By.cssSelector("span[class='taLnk ulBlueLinks']"));
            System.out.println("ASHU: moreButton size=" + moreButton.size());
            if (moreButton.size() == 0)
              return true;

            System.out.println("ASHU: moreButton text=" + moreButton.get(0).getText());
            if (moreButton.get(0).getText().equals("More")) {
              moreButton.get(0).click();
              waitForReviewsLoading(driver);
              return true;
            }
          } catch(Exception e) {
            System.out.println("ASHU: error in click on More" + e);
            return false;
          }
          return true;
        }
      });
    } catch (Exception e) {
      System.out.println("ASHU: error in function clickOnMore" + e);
    }
  }

  public static void waitForReviewsLoading(WebDriver driver) {
    new WebDriverWait(driver, 10).until(new Function<WebDriver, Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        try {
          List<WebElement> moreButton = driver.findElement(By.id("REVIEWS")).findElements(By.cssSelector("span[class='taLnk ulBlueLinks']"));
          for (WebElement button: moreButton)
            if (moreButton.get(0).getText().equals("More"))
              return false;
          return true;
        } catch(Exception e) {
          System.out.println("ASHU: error in wait for reviews loading" + e);
          return false;
        }
      }
    });
  }

  public static boolean getNextButton(WebDriver driver){
    while(true) {
      try {
        List<WebElement> nextList = driver.findElement(By.id("REVIEWS")).findElements(By.cssSelector("a[class*='nav next taLnk ui_button primary']"));
        System.out.println("ASHU: nextlist size=" + nextList.size());
        if (nextList.size() == 0)
          return false;

        new WebDriverWait(driver, 10).until(new Function<WebDriver, Boolean>() {
          @Override
          public Boolean apply(WebDriver driver) {
            return nextList.get(0).isDisplayed() && nextList.get(0).isEnabled();
          }
        });

        nextList.get(0).click();
        waitForPageLoading(driver);
        System.out.println("ASHU: page loading complete");
        return true;
      } catch(Exception e) {
        System.out.println("ASHU: some error in getNextButton " + e);
        //return false;
      }
    }
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

          return docStatus && jQueryStatus;
        }
      });
    }
    catch (Exception e) {
      System.out.println("ASHU: error in wait for page loading " + e);
    }
  }
}
