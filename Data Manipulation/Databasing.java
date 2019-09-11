import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import yahoofinance.Stock;

public class Databasing {
	private static boolean recording;
	private static boolean kill;
	public static class Recording{
		public static void Stop() {
			recording=false;
		}
		public static void Retry() {
			recording=true;
			kill=false;
			System.out.println("----- Re-Booting Data-Mining System -----");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread.run();
		}
		private static Thread thread = new Thread() {
			public void run() {
				Date date = new Date();
				try {
				while(recording&&(!kill)) {
					long AstartTime = System.nanoTime();
					HashMap<String, ArrayList<Double>> dataMap = new HashMap<String, ArrayList<Double>>();
					Map<String, Stock> prestocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
					for(int i=0;i<Database.Stock_Names.size()&&(!kill);i++) {
						ArrayList<Double> list = new ArrayList<Double>();
						list.add(prestocks.get(Database.Stock_Names.get(i)).getQuote().getPrice().doubleValue());
						dataMap.put(Database.Stock_Names.get(i), list);
					}
						long AendTime = System.nanoTime();
						long AtimeElapsed = (AendTime - AstartTime)/1000000;
						if(AtimeElapsed>Database.timeInterval) {
							System.out.println("----- Resetting Databasing System... Overtime: "+(AtimeElapsed-Database.timeInterval)+" -----");
							kill=true;
						}else {
							try {
								Thread.sleep(Database.timeInterval-AtimeElapsed);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					for(int i=0;i<Database.stockDataFileAmount-1&&(!kill);i++) {
						
						long startTime = System.nanoTime();
						Map<String, Stock> stocks = Stock_Info.Current_Price_Array(Database.Stock_Names);
						for(int ie=0;ie<Database.Stock_Names.size()&&(!kill);ie++) {
							dataMap.get(Database.Stock_Names.get(ie)).add(stocks.get(Database.Stock_Names.get(ie)).getQuote().getPrice().doubleValue());
						}
						long endTime = System.nanoTime();
						long timeElapsed = (endTime - startTime)/1000000;
						if(timeElapsed>Database.timeInterval) {
							System.out.println("----- Resetting Databasing System... Overtime: "+(timeElapsed-Database.timeInterval)+" -----");
							kill=true;
						}else {
							try {
								Thread.sleep(Database.timeInterval-timeElapsed);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					for(int i=0;i<Database.Stock_Names.size()&&(!kill);i++) {
						File file = new File(Database.basedir+"/Stock Data/"+Database.Stock_Names.get(i)+"/Data/"+date.getTime());
						if(!file.exists()) {
						file.mkdir();
						}
					}
					Date filedate = new Date();
					for(int i=0;i<Database.Stock_Names.size()&&(!kill);i++) {
						Read_And_Write.File_Manipulation.WriteToFile(Read_And_Write.File_Manipulation
								.JSON_Maker(dataMap.get(Database.Stock_Names.get(i)))
								,"/Stock Data/"+Database.Stock_Names.get(i)+"/Data/"+date.getTime()+"/"+filedate.getTime());
								
					}
					if(kill) {
						Retry();
					}
				}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		public static void Start(){
			recording=true;
			kill=false;
			thread.start();
		}
	}
}
