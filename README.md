# pipeline-for-arbitrary-pairs
Given a date range and an arbitrary set of keywords, generate a set of pairs

If you want to recompile, please use " mvn install "

**Step 1: Download Archives**
  First, you will want to download a set of archives. 

Output: 
-    Argument 1: The folder you want to deploy the archives into. 

Input: 
-    Argument 2: Your wget directory; On linux, this is /usr/bin/wget. Windows will require third party software.
-    Argument 3 and 4: Date range: This designates the beginning and end of when the github miner will examine. In the format YYYY-MM-DD. 
-      Note: Github API changes. To my understanding, it's risky to go before 2018, and the hard limit is 2015

  sample bash input:
    `java -cp target/change-extractor-0.1.jar main.DownloadArchives ~/github-archive/ /usr/bin/wget 2020-01-01 2020-02-01`

  If done correctly, this should return a series of archives in the format YYY-MM-DD-#.json.gz
  
**Step 2: Improved Identify Bug Fixing Commit.**
  These archives will be scoured for various keywords.
  
  Output:
-    Argument 1: Output csv - A file containing the directions to each commit with relevant information.

Input:
-    Argument 2: Github archive output folder from part 2
-    Argument 3: Keyword csv path: Create a csv that contains all keywords that you want in the file. Each line should contain a series of comma separated words. At least one word from each line will need to be in each commit. Try filling each line with synonyms. 

  sample bash input:
    `java -cp target/change-extractor-0.1.jar:lib/gson-2.8.6.jar main.CommitKeywordSearcher subdir/identify-output.csv ~/github-archive/ keywords/bugfixes.csv`
    
  Ideally it should return a single csv file containing unintelligible strings of characters followed by github links.

**Step 3: Get Deep Learning Mutants**
  On this step, we acquire the actual java files, both before and after the edit:
  
  Misc:

-    Argument 1: Your curl path. On unix based systems, /usr/bin/curl. 

Output:
-    Argument 2: The output csv.
-    Argument 3: The output folder that will contain the filepath.

Input:
-    Argument 4: The path to the output of the previous step.
-    Argument 5: Path to your github authorization string

  sample bash input:
    `java -cp target/change-extractor-0.1.jar:lib/gson-2.8.6.jar main.GitHubAPI_GetDataDeepLearningMutants subdir/identify-output.csv ~/github-archive/ keywords/bugfixes.csv github.auth`
  
  Ideally this should return a file system filled with java files, labeled before and after.
  
