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
					while(recording) {
						HashMap<String, ArrayList<Stock>> dataMap = new HashMap<String, ArrayList<Stock>>();
						Map<String, Stock> prestocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
						for(int i=0;i<Database.Stock_Names.size();i++) {
							ArrayList<Stock> list = new ArrayList<Stock>();
							list.add(prestocks.get(Database.Stock_Names.get(i)));
							dataMap.put(Database.Stock_Names.get(i), list);
						}
						try {
							Thread.sleep(Database.timeInterval);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for(int i=0;i<Database.stockDataFileAmount-1;i++) {
							Map<String, Stock> stocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
							for(int ie=0;ie<Database.Stock_Names.size();ie++) {
								dataMap.get(Database.Stock_Names.get(ie)).add(stocks.get(Database.Stock_Names.get(ie)));
							}
							try {
								Thread.sleep(Database.timeInterval);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						for(int i=0;i<Database.Stock_Names.size();i++) {
							File file = new File(Database.basedir+"/Stock Data/"+Database.Stock_Names.get(i)+"/Data/"+date.getTime());
							if(!file.exists()) {
							file.mkdir();
							}
						}
						Date filedate = new Date();
						for(int i=0;i<Database.Stock_Names.size();i++) {
							Read_And_Write.File_Manipulation.WriteToFile(Read_And_Write.File_Manipulation
									.JSON_Maker(dataMap.get(Database.Stock_Names.get(i)))
									,"/Stock Data/"+Database.Stock_Names.get(i)+"/Data/"+date.getTime()+"/"+filedate.getTime());
									
						}
					}
				}
			};
			thread.start();
		}
	}
}
