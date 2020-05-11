package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import parser.bean.Event;

public class ParseJSON {

	public static ArrayList<Event> readEventsFromJSON(File JSON){
		
		ArrayList<Event> result = new ArrayList<Event>();
		Gson gson = new GsonBuilder().create();
		
		try(BufferedReader br = new BufferedReader(new FileReader(JSON))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	result.add(gson.fromJson(line, Event.class));
		    }
		} catch (JsonSyntaxException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
}
