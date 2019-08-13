import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

public class Stock_Info {
public static void Current_Price() throws IOException {
	Stock stock = YahooFinance.get("INTC");
	 
	BigDecimal price = stock.getQuote().getPrice();
	BigDecimal change = stock.getQuote().getChangeInPercent();
	BigDecimal peg = stock.getStats().getPeg();
	BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
	 
	stock.print();
}
}
