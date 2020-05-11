package main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import crawler.Crawler;

public class DownloadArchives {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		String destinationPath = args[0];
		String wgetPath = args[1];
		//date in the format yyyy-mm-dd
		String fromDate = args[2];
		String toDate = args[3];
		
		String[] dateTokens = fromDate.split("-");
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, Integer.valueOf(dateTokens[0]));
		cal.set(Calendar.MONTH, Integer.valueOf(dateTokens[1]));
		cal.set(Calendar.DATE, Integer.valueOf(dateTokens[2]));
		Date from = cal.getTime();
		
		dateTokens = toDate.split("-");
		
		cal.clear();
		cal.set(Calendar.YEAR, Integer.valueOf(dateTokens[0]));
		cal.set(Calendar.MONTH, Integer.valueOf(dateTokens[1]));
		cal.set(Calendar.DATE, Integer.valueOf(dateTokens[2]));
		
		Date to = cal.getTime();
		
		Crawler.downloadArchives(new File(destinationPath), from, to, wgetPath);
		//Crawler.downloadArchives(new File("/Users/gbavota/datasets/github-archive/"), from, to, "/usr/bin/wget");
		//Crawler.downloadArchives(new File("/Users/gbavota/aaa/"), from, to, "/usr/local/bin/wget");
		
	}
	
}
