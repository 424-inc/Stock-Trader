import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.io.CharSink;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import yahoofinance.Stock;

public class Read_And_Write {
	// Anything that needs to happen on Startup should be added to this method
	public static void Startup() {
		File file = new File(Database.basedir);
		if(!file.exists()) {
			file.mkdirs();
		}
		File fileAI1 = new File(Database.basedir+"/AI/Training Data/Test/Lables");
		if(!fileAI1.exists()) {
			fileAI1.mkdirs();
		}
		File fileAI2 = new File(Database.basedir+"/AI/Training Data/Test/Features");
		if(!fileAI2.exists()) {
			fileAI2.mkdirs();
		}
		File fileAI3 = new File(Database.basedir+"/AI/Training Data/Train/Lables");
		if(!fileAI3.exists()) {
			fileAI3.mkdirs();
		}
		File fileAI4 = new File(Database.basedir+"/AI/Training Data/Train/Features");
		if(!fileAI4.exists()) {
			fileAI4.mkdirs();
		}
		File stockData = new File(Database.basedir+"/Stock Data");
		if(!stockData.exists()) {
			stockData.mkdirs();
		}
		File fileSecurity = new File(Database.basedir+"/Security");
		if(!fileSecurity.exists()) {
			fileSecurity.mkdirs();
		}
		File fileLogs = new File(Database.basedir+"/logs");
		if(!fileLogs.exists()) {
			fileLogs.mkdirs();
		}
		Startup_Files.Stock_Names();
		Startup_Files.Telegram_Credentials();
	}
	static class AI{
		public static ArrayList<Double> Read_Array(String Directory){
			Type token = new TypeToken<ArrayList<Double>>(){}.getType();
		     Gson gson = new Gson();
			try {
				ArrayList<Double> ret=gson.fromJson(File_Manipulation.ReadFromFile(Directory), token);
				return ret;
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			return null;
		}
	}
	static class Startup_Files{
		public static void Telegram_Credentials() {
			File TC = new File(Database.basedir+"/Security/Telegram_Credentials.txt");
			if(TC.exists()) {
				@SuppressWarnings("serial")
				Type token = new TypeToken<Database.Telegram>(){}.getType();
			     Gson gson = new Gson();
			     try {
					Database.telegram=gson.fromJson(File_Manipulation.ReadFromFile("Security/Telegram_Credentials"), token);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}else {
				Database.Telegram e = null;
				File_Manipulation.WriteToFile(File_Manipulation.JSON_Maker(e),"Security/Telegram_Credentials");
				System.exit(0);
			}
		}
		public static void Stock_Data_Files() {
			if(!Database.Stock_Names.contains("----- Stock Name Here -----")) {
			for(int i=0;i<Database.Stock_Names.size();i++) {
				File file = new File(Database.basedir+"/Stock Data/"+Database.Stock_Names.get(i));
				File dataFile = new File(Database.basedir+"/Stock Data/"+Database.Stock_Names.get(i)+"/Data");
				if(!file.exists()) {
					file.mkdir();
				}
				if(!dataFile.exists()) {
					dataFile.mkdir();
				}
			}
			}else {
				System.out.println("Change Stock Names.txt");
				System.exit(0);
			}
		}
		public static void Stock_Names() {
			File stockNames = new File(Database.basedir+"/Stock Names.txt");
			if(stockNames.exists()) {
				@SuppressWarnings("serial")
				Type token = new TypeToken<ArrayList<String>>(){}.getType();
		        Gson gson = new Gson();
		        try {
					Database.Stock_Names = gson.fromJson(File_Manipulation.ReadFromFile("Stock Names"), token);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		        Stock_Data_Files();
			}else {
			ArrayList<String> stocks = new ArrayList<String>();
			stocks.add("----- Stock Name Here -----");
			stocks.add("----- Stock Name Here -----");
			File_Manipulation.WriteToFile(File_Manipulation.JSON_Maker(stocks), "Stock Names");
			System.exit(0);
			}
		}
	}
	static class File_Manipulation{
		static class AI{
			public static ArrayList<Double> ReadStockFile(String Directory) {
				ArrayList<Double> ret = null;
				@SuppressWarnings("serial")
				Type token = new TypeToken<ArrayList<Double>>(){}.getType();
		        Gson gson = new Gson();
				try {
					ret = gson.fromJson(File_Manipulation.ReadFromFile(Directory), token);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				};
				return ret;
			}
		}
	// Turns any object into a JSON object that can be written to a text file
	public static String JSON_Maker(Object data) {
		String gson = new GsonBuilder().setPrettyPrinting().create().toJson(data);
		return gson;
	}
	// Returns a String value that was written within a text file
	public static String ReadFromFile(String File_Name) throws FileNotFoundException {
		File file = new File(Database.basedir+"/"+File_Name+".txt"); 
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
		File file = new File(Database.basedir+"/"+File_Name+".txt");
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
