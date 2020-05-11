# pipeline-for-arbitrary-pairs
Given a date range and an arbitrary set of keywords, generate a set of pairs

If you want to recompile, please use " mvn install "

Step 1: Download Archives
First, you will want to download a set of archives. 
Output: 
  Argument 1: The folder you want to deploy the archives into. 
Input: 
  Argument 2: Your wget directory; On linux, this is /usr/bin/wget. Windows will require third party software.
  Argument 3 and 4: Date range: This designates the beginning and end of when the github miner will examine. In the format YYYY-MM-DD. 
    Note: Github API changes. To my understanding, it's risky to go before 2018, and the hard limit is 2015

sample bash input:
java -cp target/change-extractor-0.1.jar main.DownloadArchives ~/archive/ /usr/bin/wget 2020-01-01 2020-02-01

Step 2: 
