package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import parser.bean.Commit;
import parser.bean.Event;

public class Utility {

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static ArrayList<File> listf(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath(), files);
	        }
	    }
	    
	    return files;
	}
	
	
	public static File unGzip(File infile, boolean deleteGzipfileOnSuccess) throws IOException{
	    GZIPInputStream gin = new GZIPInputStream(new FileInputStream(infile));
	    FileOutputStream fos = null;
	    try {
	        File outFile = new File(infile.getParent(), infile.getName().replaceAll("\\.gz$", ""));
	        fos = new FileOutputStream(outFile);
	        byte[] buf = new byte[100000];
	        int len;
	        while ((len = gin.read(buf)) > 0) {
	            fos.write(buf, 0, len);
	        }

	        fos.close();
	        if (deleteGzipfileOnSuccess) {
	            infile.delete();
	        }
	        return outFile; 
	    } finally {
	        if (gin != null) {
	            gin.close();    
	        }
	        if (fos != null) {
	            fos.close();    
	        }
	    }       
	}
	
	public static ArrayList<Event> getCommitsWithPatternInMessage(ArrayList<Event> events, String regexOfInterest){
		ArrayList<Event> result = new ArrayList<Event>();
		
		for(Event event: events){
			if(event.getPayload() != null){
				Commit[] commits = event.getPayload().getCommits();
				if(commits != null){
					for(Commit commit: commits){
						if(commit.getMessage() != null){
							if(commit.getMessage().matches(regexOfInterest)){
								result.add(event);
								break;
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
	
	

}
