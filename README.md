# Web-Crawler
Collect web pages through Selenium, Java (Multi-threading)

Command to run Selenium Hub:
java -jar  -Dwebdriver.gecko.driver=/home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/geckodriver /home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/selenium-server-standalone-3.13.0.jar -role hub

command to run Selenium Node:
java -jar -Djava.net.preferIPv4Stack=true -Dwebdriver.gecko.driver=/home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/geckodriver /home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/selenium-server-standalone-3.13.0.jar -role node

To compile the code:
javac -cp .:../lib/* org/oneclick/avlino/alenza/*/*.java

To run the program and collect the output in a log file:
java -cp .:../lib/* org/oneclick/avlino/alenza/crawler/HelloWorld ../URLS/tripadvisor/url10 ../data/tripadvisor/ > logs20181121-file1

