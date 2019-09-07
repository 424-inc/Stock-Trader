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
		public static void Retry() {
			recording=false;
			System.out.println("----- Re-Booting Data-Mining System -----");
			Start();
		}
		public static void Start(){
			recording=true;
			Thread thread = new Thread() {
				public void run() {
					Date date = new Date();
					try {
					while(recording) {
						long AstartTime = System.nanoTime();
						HashMap<String, ArrayList<Stock>> dataMap = new HashMap<String, ArrayList<Stock>>();
						Map<String, Stock> prestocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
						for(int i=0;i<Database.Stock_Names.size();i++) {
							ArrayList<Stock> list = new ArrayList<Stock>();
							list.add(prestocks.get(Database.Stock_Names.get(i)));
							dataMap.put(Database.Stock_Names.get(i), list);
						}
							long AendTime = System.nanoTime();
							long AtimeElapsed = (AendTime - AstartTime)/1000000;
							if(AtimeElapsed>Database.timeInterval) {
								try {
									Thread.sleep(AtimeElapsed-Database.timeInterval);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}else {
								try {
									Thread.sleep(Database.timeInterval-AtimeElapsed);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						for(int i=0;i<Database.stockDataFileAmount-1;i++) {
							
							long startTime = System.nanoTime();
							Map<String, Stock> stocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
							for(int ie=0;ie<Database.Stock_Names.size();ie++) {
								dataMap.get(Database.Stock_Names.get(ie)).add(stocks.get(Database.Stock_Names.get(ie)));
							}
							long endTime = System.nanoTime();
							long timeElapsed = (endTime - startTime)/1000000;
							if(timeElapsed>Database.timeInterval) {
								try {
									Thread.sleep(timeElapsed-Database.timeInterval);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}else {
								try {
									Thread.sleep(Database.timeInterval-timeElapsed);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
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
					}catch(Exception e) {
						Retry();
					}
				}
			};
			thread.start();
		}
	}
}
