package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import com.google.gson.Gson;

import utility.Utility;
import utility.gitUtilities.GitUtilities;
import utility.jsonBean.CommitJsonObject;
import utility.jsonBean.FileJsonBean;

public class GitHubAPI_GetDataDeepLearningMutants {

	public static void main(String[] args) throws IOException, InterruptedException {

		String curlPath = args[0];
		String outputFilePath = args[1];
		String commitsFilePath = args[2];
		String codeFolderPath = args[3];

		String authFilePath = args[4];
		int maximumNumberOfModifiedFiles = 5;
		
		File commitsFile = new File(commitsFilePath);
		File outputFile = new File(outputFilePath);
		PrintWriter pw = new PrintWriter(outputFile);
		String authToken= "";
		File authFile = new File(authFilePath);
		if (authFile.isFile() )
		{
			try (BufferedReader br = new BufferedReader(new FileReader(authFile))) {
				String line = br.readLine();
				authToken=line;
			}
		}
		int requestsDone = 0;
 		if(commitsFile.isFile()) 
			System.out.println("commit file is file");	
		if (commitsFile.getName().endsWith(".csv"))
			System.out.println("ends with csv");    
		if (commitsFile.isFile() && commitsFile.getName().endsWith(".csv")) {
			System.out.println("mining " + commitsFile.getName() + " file");

			try (BufferedReader br = new BufferedReader(new FileReader(commitsFile))) {
				for (String line; (line = br.readLine()) != null;) {
					requestsDone++;
					
					//checks every 200 requests
					if(requestsDone % 200 == 0){
						int requestsRemaining = GitUtilities.getRemainingAPIrequests(authToken, curlPath);
						System.out.println(requestsRemaining + " API requests remaining");
						while(requestsRemaining < 300){
							//it sleeps for 5 minutes
							System.out.println("Sleeping for 5 minutes");
							Thread.sleep(300000);
							requestsRemaining = GitUtilities.getRemainingAPIrequests(authToken, curlPath);
						}
					}
					
					String[] lineTokens = line.split(",");
					System.out.println("Analyzing commit: " + lineTokens[0]);
					try {

						String commitDiff = GitUtilities.getDiffWithGitHubAPI(authToken, lineTokens[0], lineTokens[1].replace(".dev", ".com"), curlPath);
						CommitJsonObject commit = new Gson().fromJson(commitDiff, CommitJsonObject.class);
						if(commit==null || commit.getFiles() == null)
							continue;
						
						FileJsonBean[] files = commit.getFiles();
						if(files.length == 0)
							continue;
						
						int numberOfModifiedJavaFiles = 0;
						for(FileJsonBean file: files){
							if(file.getFilename().endsWith(".java")){
								numberOfModifiedJavaFiles++;
							}
						}
						
						if(numberOfModifiedJavaFiles == 0 || numberOfModifiedJavaFiles > maximumNumberOfModifiedFiles)
							continue;
						
						System.out.println(numberOfModifiedJavaFiles + " modified Java files");
						
						File preFolder = new File(codeFolderPath + lineTokens[0] + "/P_dir/");
						File postFolder = new File(codeFolderPath + lineTokens[0] + "/F_dir/");
						preFolder.mkdirs();
						postFolder.mkdirs();
						
						for(FileJsonBean file: files){
							if(file.getFilename().endsWith(".java")){
								//create download folders
								String[] filePathTokens = file.getFilename().split("/");
								if(filePathTokens.length > 1){
									String filePath = file.getFilename().replaceAll(filePathTokens[filePathTokens.length-1], "");
									File pre = new File(preFolder.getAbsolutePath() + "/" + filePath);
									File post = new File(postFolder.getAbsolutePath() + "/" + filePath);
									pre.mkdirs();
									post.mkdirs();
								}
								
								GitUtilities.downloadAFileInAcommit(authToken, true, file.getFilename(), file.getRaw_url(), curlPath, 
										preFolder.getAbsolutePath() + "/" + file.getFilename());
								
								GitUtilities.downloadAFileInAcommit(authToken, false, file.getFilename(), file.getRaw_url(), curlPath, 
										postFolder.getAbsolutePath() + "/" + file.getFilename());
							}
						}
						
						pw.println(line + "," + numberOfModifiedJavaFiles);
						
						
					} catch (InterruptedException e) {
						e.printStackTrace();
						continue;
					} 
				}
			}
		}
		else{
			
			                        System.out.println("Please ensure the commit is a csv and is your second argument");
		}
		pw.close();

	}
	
	
	public static boolean containsJavaFile(String folderPath){
		ArrayList<File> filesInTheFolder = new ArrayList<File>();
		filesInTheFolder = Utility.listf(folderPath, filesInTheFolder);
		for(File f: filesInTheFolder){
			if(f.getName().endsWith(".java"))
				return true;
		}
		
		return false;
	}
	
}
