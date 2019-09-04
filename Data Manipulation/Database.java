import java.util.ArrayList;
import java.util.HashMap;

import yahoofinance.Stock;

public class Database {
	// Data collecting
		public static long timeInterval = 3000;
	// 200 = 10 mins in each file.. scans every 3 seconds
		public static int stockDataFileAmount = 200;
	
	//Graphing
		//Hours
		public static int chartTime=1;
	
	public static String basedir = System.getProperty("user.home")+"/Desktop/StockTrader";
	public static ArrayList<String> Stock_Names;
	public static Telegram telegram;
	public static HistoricStockData historicStockData;
	class Telegram{
		String username;
		String password;
	}
	class HistoricStockData{
		HashMap<String, ArrayList<Stock>> lastHour = new HashMap<String, ArrayList<Stock>>();
		HashMap<String, ArrayList<Stock>> last24Hours = new HashMap<String, ArrayList<Stock>>();
		HashMap<String, ArrayList<Stock>> last7Days = new HashMap<String, ArrayList<Stock>>();
	}
}
