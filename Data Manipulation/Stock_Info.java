import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;

public class Stock_Info {
public static BigDecimal Current_Price(String Stock) throws IOException {
	Stock stock = YahooFinance.get(Stock);
	 
	return stock.getQuote().getPrice();
}
public static ArrayList<HistoricalQuote> Historical_Price(String Stock, int Years) throws IOException {
	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();
	from.add(Calendar.YEAR, -Years);

	Stock google = YahooFinance.get(Stock);
	return (ArrayList<HistoricalQuote>) google.getHistory(from, to, Interval.DAILY);
}
}
