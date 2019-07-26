import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.google.common.io.CharSink;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;

public class Read_And_Write {
	// Anything that needs to happen on Startup should be added to this method
	public static void Startup() {
		File file = new File(Run.basedir);
		if(!file.exists()) {
			file.mkdirs();
		}
		File fileLogs = new File(Run.basedir+"/logs");
		if(!fileLogs.exists()) {
			fileLogs.mkdirs();
		}
	}
	static class File_Manipulation{
	// Turns any object into a JSON object that can be written to a text file
	public static String JSON_Maker(Object data) {
		String gson = new GsonBuilder().setPrettyPrinting().create().toJson(data);
		return gson;
	}
	// Returns a String value that was written within a text file
	public static String ReadFromFile(String File_Name) throws FileNotFoundException {
		File file = new File(Run.basedir+"/"+File_Name+".txt"); 
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file); 
		String data = "";
		
		while(sc.hasNextLine()) {
			data+= sc.nextLine();
		}
		return data;
	}
	// Writes a String value to a file with a given directory
	public static void WriteToFile(String data, String File_Name) {
		File file = new File(Run.basedir+"/"+File_Name+".txt");
	    CharSink sink = Files.asCharSink(file, StandardCharsets.UTF_8);
	    try {
	        sink.write(data);
	    }
	    catch(Exception ex)
	    {
	     System.out.println(ex);
	    }
	}
	}
}
