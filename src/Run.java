import java.io.IOException;
import java.util.ArrayList;

import yahoofinance.histquotes.HistoricalQuote;

public class Run {
	// Base directory so that all of the Information is stored in an organized manner
	// Desktop for now for simplicity
	public static String basedir = System.getProperty("user.home")+"/Desktop/StockTrader";
	public static void main(String[] args) throws IOException {
		Read_And_Write.Startup();
		// Simple example to use Yahoo Services
		while(true) {
		System.out.println(Stock_Info.Current_Price("BTC-USD"));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}

}
