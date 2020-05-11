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

public class IdentifyBugFixingCommits {

	public static void main(String[] args) {
		
		String outputFile = args[0];
		String githubArchivePath = args[1];
		
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
										if((lowerCaseMessage.contains("solve") || lowerCaseMessage.contains("fix")) && (
												lowerCaseMessage.contains("bug") || lowerCaseMessage.contains("issue") || lowerCaseMessage.contains("problem")
												 || lowerCaseMessage.contains("error"))){
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

}
