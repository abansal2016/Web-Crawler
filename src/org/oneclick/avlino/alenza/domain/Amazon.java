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

public class Amazon extends BaseDomain {

  public void getHtml(WebDriver driver, int pagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;
    int pageNo = 1;
    int pageCount = 0;

    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    String reviews;
    try {
      do {
        errorMsg = "";
        url = driver.getCurrentUrl() + "-->AlenzaCustomPageNo=" + pageNo++;
        //errorMsg+= "URL=" + url + "\n";

        //reviews = (String) jsx.executeScript("return document.getElementById('REVIEWS').outerHTML");
        //obj.put(url, reviews);
        obj.put(url, driver.getPageSource());
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
      printMessage(e, "Error in main loop");
      //System.out.println("ASHU: error in main loop for " url + e);
    }
  }

  public void dumpHtmlToFile(JSONObject obj, String outputPath, int fileNo) {
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

  public boolean getNextButton(WebDriver driver) {
    while (true) {
      try {
        List<WebElement> nextList = driver.findElements(By.cssSelector("li[class='a-last']"));
        System.out.println("URL= " + url);
        System.out.println("Nextlist size=" + nextList.size());
        if (nextList.size() == 0)
          return false;

        nextList.get(0).click();
        waitForPageLoading(driver);
        return true;
      } catch(Exception e) {
        printMessage(e, "Some error in getNextButton");
        //return false;
      }
    }
  }

  public void waitForPageLoading(WebDriver driver){
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
          List<WebElement> nextList = driver.findElements(By.cssSelector("li[class='a-last']"));
          if (nextList.size() != 0)
            buttonStatus = true;

          return docStatus && jQueryStatus && buttonStatus;
          //return docStatus && jQueryStatus;
        }
      });
    } catch (Exception e) {
      printMessage(e, "Error in wait for page loading");
    }
  }
}
