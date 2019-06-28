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

public class Safari extends BaseDomain {

  String ansList = "5300000";
  int currQues = 0;

  public void getHtml(WebDriver driver, int pagesToStore, String outputPath) {

    JSONObject obj = new JSONObject();
    int fileNo = 1;
    int pageNo = 1;
    int pageCount = 0;
    //String ansList = "6411111";
    //int currQues = 0;

    JavascriptExecutor jsx = (JavascriptExecutor) driver;
    String quesBox;
    waitForPageLoading(driver);
    try {
      do {
        errorMsg = "";
        url = driver.getCurrentUrl() + "-->AlenzaCustomPageNo=" + pageNo++;
        //errorMsg+= "URL=" + url + "\n";

        try {
        quesBox = (String) jsx.executeScript("return document.getElementsByClassName('component safaricom-portal-casebase-content')[0].outerHTML");
        obj.put(url, quesBox);
        } catch (Exception e) {
          obj.put(url, driver.getPageSource());
        }

        //obj.put(url, driver.getPageSource());
        pageCount++;
        if (pageCount == pagesToStore) {
          dumpHtmlToFile(obj, outputPath, fileNo++);
          obj = new JSONObject();
          pageCount = 0;
        }
        selectAnswers(driver);
      } while(getNextButton(driver));

      if (pageCount > 0)
        dumpHtmlToFile(obj, outputPath, fileNo);

    } catch (Exception e) {
      printMessage(e, "Error in main loop");
      //System.out.println("ASHU: error in main loop for " url + e);
    }
  }

  public void selectAnswers(WebDriver driver) {
    //String questionText = "";
    JSONObject ques_data = new JSONObject();
    List<WebElement> questions = driver.findElements(By.className("question"));

    if (questions.size() == 0) {
      //WebElement solution = driver.findElement(By.cssSelector("div[class='egce-box-wrap egce-p15']"));

      WebElement solutionTitle = driver.findElement(By.cssSelector("div[class='egce-float-left egce-bold']"));
      System.out.println("\nSolution Title = " + solutionTitle.getText());

      WebElement solutionText = driver.findElement(By.cssSelector("div[class='egce-m5-top egce-font-medium egce-styled-content egce-anchor-link']"));
      System.out.println("Solution Text = " + solutionText.getText());

      return;
    }

    for(WebElement ques: questions) {
      ques_data.put("question", ques.findElement(By.className("selfservice-gh-question-title")).getText());

      List<WebElement> change_ans = ques.findElements(By.className("change-ans"));
      //System.out.println("change_ans_len = " + change_ans.size());

      if (change_ans.size() == 0) {
        System.out.println("\n\nQuestion: " + ques.findElement(By.className("selfservice-gh-question-title")).getText());
        List<WebElement> answers = ques.findElements(By.className("answer"));
        int i = 1;
        for (WebElement ans: answers) {
          System.out.println("\tOption " + i + ": " + ans.getText());
          ques_data.put("ans" + i, ans.getText());
          //System.out.println("outside answer value = " + ansList.charAt(currQues));
          /*if (Character.getNumericValue(ansList.charAt(currQues)) == i) {
            //System.out.println("answer value = " + ansList.charAt(currQues));
            ans.findElement(By.tagName("input")).click();
            System.out.println("Selected Option: " + ans.getText());
            ques_data.put("selected_ans", ans.getText());
            currQues++;
          }*/
          i++;
        }
        WebElement selectedAns = answers.get(Character.getNumericValue(ansList.charAt(currQues++)));
        selectedAns.findElement(By.tagName("input")).click();
        System.out.println("\n\tSelected Option: " + selectedAns.getText());
        ques_data.put("selected_ans", selectedAns.getText());
        //System.out.println(ques_data);
      }
      /*List<WebElement> answerRadio = ques.findElements(By.tagName("input"));
      
      if (answerRadio.get(2).isDisplayed() && answerRadio.get(2).isEnabled())
        answerRadio.get(2).click();*/
      //System.out.println(ques_data);
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
    //return false;
    while (true) {
      try {
        List<WebElement> doneList = driver.findElements(By.className("done-button"));
        //System.out.println("URL= " + url);
        //System.out.println("donelist size=" + doneList.size());
        if (doneList.size() == 0)
          return false;

        doneList.get(0).click();
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
          List<WebElement> doneList = driver.findElements(By.className("done-button"));
          if (doneList.size() != 0)
            buttonStatus = true;

          //System.out.println("docStatus" + docStatus);
          //System.out.println("jQueryStatus" + jQueryStatus);
          //System.out.println("buttonStatus" + buttonStatus);

          return docStatus && jQueryStatus && buttonStatus;
          //return docStatus && jQueryStatus;
        }
      });
    } catch (Exception e) {
      //printMessage(e, "Error in wait for page loading");
    }
  }
}
