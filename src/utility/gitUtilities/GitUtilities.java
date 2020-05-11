package utility.gitUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GitUtilities {

	
	public static String getDiffWithGitHubAPI(String authToken, String commit, String apiAddress, String curlPath) throws IOException, InterruptedException{
		Runtime rt = Runtime.getRuntime();
		
		String cmd = curlPath + " -u username:" + authToken + " " + apiAddress + "/compare/" + commit + "%5E..." + commit; 
		
		Process process = rt.exec(cmd);

		String line = null;
		String output = "";
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		while ((line = stdoutReader.readLine()) != null) {
			output += line + "\n";
		}

		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		while ((line = stderrReader.readLine()) != null) {
			//System.out.println(line);
		}

		process.waitFor();
		// [END]
		return output;
	}

	
	public static void downloadAFileInAcommit(String authToken, boolean previousCommit, String fileName, String rawUrl, String curlPath, String outputPath) throws IOException, InterruptedException{
		Runtime rt = Runtime.getRuntime();
		
		//String cmd = curlPath + " -u username:" + authToken + " " + rawUrl.replaceAll("https://github.com/", "https://raw.githubusercontent.com/").replaceAll("/raw/", "/") + 
			//	 " -o " + outputPath;
		
		String cmd = curlPath + " " + rawUrl.replaceAll("https://github.com/", "https://raw.githubusercontent.com/").replaceAll("/raw/", "/") + 
				" -o " + outputPath;
		
		if(previousCommit){
			cmd = cmd.replaceFirst("/" + fileName, "%5E/" + fileName);
		}
		
		//System.out.println(cmd);
		
		Process process = rt.exec(cmd);

		String line = null;
		
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		while ((line = stdoutReader.readLine()) != null) {
			//output += line + "\n";
		}

		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		while ((line = stderrReader.readLine()) != null) {
			//System.out.println(line);
		}

		process.waitFor();
		// [END]
	}
	
	public static int getRemainingAPIrequests(String authToken, String curlPath) throws IOException, InterruptedException{
		
		Runtime rt = Runtime.getRuntime();
		
		String cmd = curlPath + " -u username:" + authToken + " -i https://api.github.com/"; 
		
		Process process = rt.exec(cmd);

		String line = null;
		
		
		
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		while ((line = stdoutReader.readLine()) != null) {
			if(line.contains("X-RateLimit-Remaining: ")){
				return Integer.valueOf(line.replaceAll("X-RateLimit-Remaining: ", ""));
			}
		}

		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		while ((line = stderrReader.readLine()) != null) {
			//System.out.println(line);
		}

		process.waitFor();
		// [END]
		
		return 0;
	}
	
}
