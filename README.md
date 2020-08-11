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






Update on 11 Aug 2020:

java -jar -Dwebdriver.gecko.driver=/home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/geckodriver /home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/selenium-server-standalone-3.13.0.jar -role hub &

java -jar -Djava.net.preferIPv4Stack=true -Dwebdriver.gecko.driver=/home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/geckodriver /home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/selenium-server-standalone-3.13.0.jar -role node &

java -jar -Djava.net.preferIPv4Stack=true -Dwebdriver.gecko.driver=/home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/geckodriver /home/abhishek.bansal/AlenzaWebDataCollection/selenium-req/selenium-server-standalone-3.13.0.jar -role node -hub http://192.168.122.1:4444/grid/ &

-maxSession 2

killall geckodriver

javac -cp .:../lib/* org/oneclick/avlino/alenza/*/*.java

java -cp .:../lib/* org/oneclick/avlino/alenza/crawler/WebCrawler ../urls/service-centre/complete-star-url ../data/servicecentre/ > ../logs/service-centre/logs20200206-file02


grep StoreNo logs20200718-file06 | sed 's+https://www.google.com/maps/search/++g' | sed 's/service+centre+//g' | sed 's/phone:tel://g' | sed 's/Address: //g' | sed 's/StoreNo=//g' | sed 's/vivo+/v+/g' | sed 's/oppo+/o+/g' | sed 's/samsung+/s+/g' | sed 's/xiaomi+/x+/g' > logs20200718-file06-extract


Change in prog:
pick url from input file to put in output file 			-->done
shorting of text as above: 								-->done
cron job to run job monthly
cron job to send a mail once job completes
check how more instances of browser can be run 
remove big file from git 								-->done

