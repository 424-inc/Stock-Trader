import java.util.ArrayList;
import java.util.HashMap;

import yahoofinance.Stock;

public class Database {
	public static long timeInterval = 3000;
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
