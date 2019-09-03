import java.util.ArrayList;

import yahoofinance.Stock;

public class Statistics {
	public static void AvRate(ArrayList<Stock> Data){
		ArrayList<Double> differences = new ArrayList<Double>();
		differences.add(Data.get(0).getQuote().getPrice().doubleValue()-Data.get(1).getQuote().getPrice().doubleValue());
		for(int i=1;i<Data.size();i++) {
			differences.add(Data.get(i+1).getQuote().getPrice().doubleValue()-Data.get(i).getQuote().getPrice().doubleValue());
		}
		double total = 0;
		for(int i=0;i<differences.size();i++) {
			total+=differences.get(i);
		}
		total=total/differences.size();
		System.out.println(total);
	}
}
