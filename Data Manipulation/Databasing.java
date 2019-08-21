import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import yahoofinance.Stock;

public class Databasing {
	private static boolean recording;
	public static class Recording{
		public static void Stop() {
			recording=false;
		}
		public static void Start(){
			recording=true;
			Thread thread = new Thread() {
				public void run() {
					Date date = new Date();
					HashMap<String, ArrayList<Stock>> dataMap = new HashMap<String, ArrayList<Stock>>();
					Map<String, Stock> prestocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
					for(int i=0;i<Database.Stock_Names.size();i++) {
						ArrayList<Stock> list = new ArrayList<Stock>();
						list.add(prestocks.get(Database.Stock_Names.get(i)));
						dataMap.put(Database.Stock_Names.get(i), list);
						File file = new File(Database.basedir+"/Stock Data/"+Database.Stock_Names.get(i)+"/Data/"+date);
						file.mkdir();
					}
					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					while(recording) {
						for(int i=0;i<60;i++) {
							Map<String, Stock> stocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
							for(int ie=0;ie<Database.Stock_Names.size();ie++) {
								dataMap.get(Database.Stock_Names.get(ie)).add(stocks.get(Database.Stock_Names.get(ie)));
							}
							try {
								Thread.sleep(1000*60);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			thread.start();
		}
	}
}
