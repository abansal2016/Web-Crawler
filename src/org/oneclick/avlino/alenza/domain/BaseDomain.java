package org.oneclick.avlino.alenza.domain;

class BaseDomain {
  String url;
  String errorMsg;

  protected BaseDomain() {
    url = null;
    errorMsg = null;
    System.out.println("BaseDomain constructor called");
  }

  protected void printMessage(Exception e, String msg) {
    System.out.println("\n\n----ASHU: Error Starts----");
    System.out.println(msg);
    System.out.println("URL is " + url);
    System.out.println("Module Stacktrace is as below:\n" + errorMsg);
    System.out.println(e.toString()); //e.getMessage());
    System.out.println("----ASHU: Error Ends----");
    errorMsg = "";
  }
}
