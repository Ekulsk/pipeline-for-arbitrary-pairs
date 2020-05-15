# pipeline-for-arbitrary-pairs:
Given a date range and an arbitrary set of keywords, generate a set of pairs

Authors: modified by Lucca Skon, original author: Michele Tufano

If you want to recompile, please use " mvn install "

**Step 1: Download Archives**
  First, you will want to download a set of archives. 

-    Argument 1: Your wget directory; On linux, this is /usr/bin/wget. Windows will require third party software.
-    Argument 2: The folder you want to deploy the archives into. 
-    Argument 3 and 4: Date range: This designates the beginning and end of when the github miner will examine. In the format YYYY-MM-DD. 
-      Note: Github API changes. To my understanding, it's risky to go before 2018, and the hard limit is 2015

  sample bash input:
    `java -cp target/pipeline-for-arbitrary-pairs-0.1.jar main.DownloadArchives /usr/bin/wget ~/github-archive/ 2020-01-01 2020-02-01`

  If done correctly, this should return a series of archives in the format YYY-MM-DD-#.json.gz
  
**Step 2: Commit Keyword Searcher.**
  These archives will be scoured for various keywords.
  
-    Argument 1: Github archive output folder from part 2
-    Argument 2: Keyword csv path: Create a csv that contains all keywords that you want in the file. Each line should contain a series of comma separated words. At least one word from each line will need to be in each commit. Try filling each line with synonyms. 
-    Argument 3: Output csv - A file containing the directions to each commit with relevant information.

  sample bash input:
    `java -cp target/pipeline-for-arbitrary-pairs-0.1.jar:lib/gson-2.8.6.jar main.CommitKeywordSearcher ~/github-archive/ keywords/bugfixes.csv subdir/identify-output.csv`
    
  Ideally it should return a single csv file containing unintelligible strings of characters followed by github links.

**Step 3: Get Deep Learning Mutants**
  On this step, we acquire the actual java files, both before and after the edit:
  
-    Argument 1: Your curl path. On unix based systems, /usr/bin/curl. 
-    Argument 2: The path to the output of the previous step.
-    Argument 3: The output csv.
-    Argument 4: The output folder that will contain the filepath. **make sure this ends with a /, or they will not be placed in the same directory**
-    Argument 5: Path to your github authorization string. go to https://github.com/settings/tokens and copy your token into a file.

  sample bash input:
    `java -cp target/pipeline-for-arbitrary-pairs-0.1.jar:lib/gson-2.8.6.jar main.GitHubAPI_GetDataDeepLearningMutants /usr/bin/curl subdir/identify-output.csv subdir/deep-learning-output.csv subdir/code-sets/ github.auth`
  
  Ideally this should return a file system filled with java files, labeled before and after.
  
**Step 4: Create Arbitrary Keyword Pairs**
  On this step, you will be transforming the java collection into something a bit more readable.
  
  - Argument 1: The root folder of the previous output.
  - Argument 2: Output directory for pairs.
  - Argument 3: (optional) Output csv containing all method pairs.
  
  sample bash input:
    `java -cp target/pipeline-for-arbitrary-pairs-0.1.jar:lib/gumtree-spoon-ast-diff.jar extractor.main.Main subdir/code-sets/ subdir/pairs/ subdir/output.csv`

This should leave you with a filesystem of pairs
