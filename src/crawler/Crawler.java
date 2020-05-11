package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Crawler {

	public static boolean downloadArchives(File where, Date from, Date to, String wgetPath) 
				throws IOException, InterruptedException{
		
		if(to.before(from))
			return false;
		
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(from);
	    Calendar toCal = Calendar.getInstance();
		toCal.setTime(to);
	    
	    do{
	    	fromCal.setTime(from);
	    	Integer yearFrom = fromCal.get(Calendar.YEAR);
	    	Integer monthFrom = fromCal.get(Calendar.MONTH);		    
	    	Integer dayFrom = fromCal.get(Calendar.DAY_OF_MONTH);
		    
		    String monthString = Integer.toString(monthFrom);
		    if(monthString.length() < 2)
		    	monthString = "0" + monthString;
		    
		    if(monthString.equals("11"))//11 is december in the Calendar object
		    	monthString = "12";
		    
		    String dayString = Integer.toString(dayFrom);
		    if(dayString.length() < 2)
		    	dayString = "0" + dayString;
		    
		    for(int i=0; i<24; i++){
		    		String dateToDownload = yearFrom + "-" + monthString + "-" + dayString + "-" + i + ".json.gz";
		    		String link = "http://data.githubarchive.org/" + dateToDownload;
		    	
		    		ProcessBuilder myProc2 = new ProcessBuilder(wgetPath, "-P", where.getAbsolutePath(), link);
		    		myProc2.redirectErrorStream(true);        
		    		final Process process = myProc2.start();
		    		InputStream myIS = process.getInputStream();
		    		String tempOut = convertStreamToStr(myIS);
		    	
		    		System.out.println(link);
				
		    }
		    
	    	from.setTime(from.getTime() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
	    } while(from.before(to));
	    
		return true;
	}
	
	
	public static String convertStreamToStr(InputStream is) throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
}
