package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import parser.ParseJSON;
import parser.bean.Commit;
import parser.bean.Event;
import utility.Utility;

import java.io.FileReader;
import java.io.BufferedReader;

public class CommitKeywordSearcher {

	/*
	 * * Sample usage: (in mine-bug-fixing-commits base directory)
	 * * java -cp target/classes/ main.IdentifyBugFixingCommits output.csv path/to/archive/ keywords.csv
	 * * Please use csv files for output and keywords.
	 * **************************************************/
	static ArrayList<String[]> keywords;
	public static void main(String[] args) throws IOException {
		
		String outputFile = args[0];
		String githubArchivePath = args[1];
		String keywordFilePath = args[2]; // This is where the keywords have been stored.
		keyWordSet(keywordFilePath);
						
		File commitMessagesOfInterest = new File(outputFile);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(commitMessagesOfInterest);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		File[] downloaded = new File(githubArchivePath).listFiles();
		int bugFixingRelated = 0;
		int overall = 0;
		
		Arrays.sort(downloaded);
		
		for(File json: downloaded){
			if(!json.getName().equals(".DS_Store") && json.getName().endsWith(".gz")){
				System.out.println(json.getName());
				try {
					Utility.unGzip(json, false);
				} catch (IOException e) {
					continue;
				}
				ArrayList<Event> events = ParseJSON.readEventsFromJSON(new File(json.getAbsolutePath().replaceAll("\\.gz$", "")));
				if(events == null)
					continue;
				new File(json.getAbsolutePath().replaceAll("\\.gz$", "")).delete();
				for(Event event: events){
					if(event.getRepo().getUrl().toLowerCase().contains("refact"))
						continue;
					if(event.getType().equals("PushEvent")){
						overall++;
						if(event.getPayload() != null){
							Commit[] commits = event.getPayload().getCommits();
							if(commits != null){
								for(Commit commit: commits){
									if(commit.getMessage() != null && commit.getMessage().length() < 400){
										String lowerCaseMessage = commit.getMessage().toLowerCase();
										if(keywordSearch(lowerCaseMessage)){
											bugFixingRelated++;
											pw.println(commit.getSha() + "," + event.getRepo().getUrl() + "," + event.getRepo().getUrl().replace("api.", "").replace(".dev", ".com").replace("repos/", "") + "/commit/" + commit.getSha() + "," + commit.getMessage().replaceAll("\\r\\n|\\r|\\n", " ").replace(",", " "));
											
										} 
										
									}
								}
							}
						}
					}
				}
			}
		}
		
		System.out.println(overall + " events");
		System.out.println(bugFixingRelated + " bug-fixing events");
		pw.close();
		

	}
	
	public static void keyWordSet(String filename) throws IOException{//In the csv, each line will contain interchangeable words. One word from each line is required.
		File file = new File(filename);
		if (file.isFile() && file.getName().endsWith(".csv"))
		{
			keywords = new ArrayList<String[]>();
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				for (String line; (line = br.readLine()) != null;) {
					keywords.add(line.replaceAll("[^A-Za-z,]+", "").toLowerCase().split(","));
				}
			}
		}
	}

	public static boolean keywordSearch(String message){
	// Returns true if at least one keyword from each line is in the output.
		for (String[] inter:keywords){
			boolean shouldProceed = false;
			for (String keyword:inter){
				if(message.contains(keyword))	{
					shouldProceed = true;
				}
			}
			if(!shouldProceed){
				return false;
			}
		}
		return true;
	}


}
